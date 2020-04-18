package game;
/*
Author: Imri
Date: 2020-02-01
*/

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Server.
 * The server has a default board size.
 * Each player can set their desired board size, or number less than 1 to use the server's default.
 * If they choose non-default size, they'll have to wait for another player to choose the exact size.
 */
public class Server {
    private static final int CARD_REVEAL_TIME = 3000; // 3 seconds

    private ServerSocket serverSocket;
    private int defaultBoardSize; // Default number of cards to play
    private boolean keepRunning;
    private ConcurrentHashMap<UUID, Connection> connections; // Current connections, which could be waiting/playing.
    private ConcurrentHashMap<UUID, GameInstance> games; // Current games in play.
    private ConcurrentHashMap<Integer, Connection> waitingPlayers; // We use the key as the requested number of cards to play.
    // I know this is an overkill, but I wanted to hone my skills doing things of this sort.

    /**
     * Instantiates a new Server.
     *
     * @param port the port
     */
    public Server(int port, int defaultBoardSize) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.defaultBoardSize = defaultBoardSize;
        connections = new ConcurrentHashMap<>();
        games = new ConcurrentHashMap<>();
        waitingPlayers = new ConcurrentHashMap<>();
        this.keepRunning = true;
    }

    /**
     * Run.
     */
    public void run() {
        System.out.println("Server running");
        Socket socket;
        while (keepRunning) {
            try {
                socket = serverSocket.accept();
                Connection connection = new Connection(UUID.randomUUID(), socket);
                connections.put(connection.getClientId(), connection);
                System.out.println("New connection. Total: " + connections.size());
                new Thread(new ConnectionThread(connection)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private ArrayList<Integer> createPermutation(int size) {
        SecureRandom random = new SecureRandom();
        int[] ids = new int[Constants.UNIQUE_CARDS];
        for (int i = 0; i < Constants.UNIQUE_CARDS; i++) {
            ids[i] = i;
        }
        // Efficient selection of 'size' random distinct cards
        // We choose one at random, then putting it to the last index so we won't choose it again.
        // Then we add the card twice (because each card appears twice on board), and finally shuffling all the selected
        ArrayList<Integer> cardsOrder = new ArrayList<>(size);
        for (int i = Constants.UNIQUE_CARDS-1; i >= Constants.UNIQUE_CARDS - (size/2); i--) {
            int rand = random.nextInt(i+1);
            int tmp = ids[i];
            ids[i] = ids[rand];
            ids[rand] = tmp;
            cardsOrder.add(ids[i]);
            cardsOrder.add(ids[i]);
        }
        // Now shuffle the selected cards and return it
        Collections.shuffle(cardsOrder);
        return cardsOrder;
    }

    private void addWaitingPlayer(Connection connection, Integer boardSize) {
        // Check if there is already someone waiting for same boardSize game
        Connection other = waitingPlayers.get(boardSize);
        // No available player, so put on waiting list
        if (other == null) {
            waitingPlayers.put(boardSize, connection);
        }
        else {
            // There are 2 players waiting for the same card amount!
            // Remove other from queue
            waitingPlayers.remove(boardSize);
            UUID gameId = UUID.randomUUID();
            GameInstance game = new GameInstance(gameId, other, connection, createPermutation(boardSize));
            other.setGameId(gameId);
            connection.setGameId(gameId);

            games.put(gameId, game);
            startGame(game);
        }
    }

    private void startGame(GameInstance game) {
        ServerResponse res = ResponseCreator.createJoin(game.getCardsOrder());
        // Send the board ordering to both players
        res.setClientId(game.getPlayer1().getClientId());
        sendResponse(game.getPlayer1(), res);
        res.setClientId(game.getPlayer2().getClientId());
        sendResponse(game.getPlayer2(), res);
        // Start the game. The first player in queue begins
        game.setPlayingPlayer(game.getPlayer1());
        game.setWaitingPlayer(game.getPlayer2());
        game.getPlayer1().setPlaying(true);
        game.getPlayer2().setPlaying(true);
        // Notify players of their turn status
        sendResponse(game.getPlayer1(), ResponseCreator.createPlayerTurn());
        sendResponse(game.getPlayer2(), ResponseCreator.createOpponentTurn());
        // Initialize score
        game.getPlayer1().setScore(0);
        game.getPlayer2().setScore(0);
        updateScore(game);
    }

    private void sendResponse(Connection connection, ServerResponse res) {
        try {
            connection.getOutputStream().writeObject(res);
            connection.getOutputStream().flush();
        } catch (IOException ignored) { }
    }

    private void updateScore(GameInstance game) {
        ServerResponse res = ResponseCreator.createSetScore();
        // The caller is the playing player, so check whether its player1 or player2, and send accordingly
        if (game.getPlayingPlayer().equals(game.getPlayer1())) {
            res.setPlayerScore(game.getPlayer1().getScore());
            res.setOpponentScore(game.getPlayer2().getScore());
            sendResponse(game.getPlayer1(), res);

            res.setPlayerScore(game.getPlayer2().getScore());
            res.setOpponentScore(game.getPlayer1().getScore());
            sendResponse(game.getPlayer2(), res);
        }
        else {
            res.setPlayerScore(game.getPlayer2().getScore());
            res.setOpponentScore(game.getPlayer1().getScore());
            sendResponse(game.getPlayer2(), res);

            res.setPlayerScore(game.getPlayer1().getScore());
            res.setOpponentScore(game.getPlayer2().getScore());
            sendResponse(game.getPlayer1(), res);
        }
    }

    private void updateTurns(GameInstance game) {
        ServerResponse res = new ServerResponse(ServerResponse.ResponseType.PLAYER_TURN);
        sendResponse(game.getPlayingPlayer(), res);
        res = new ServerResponse(ServerResponse.ResponseType.OPPONENT_TURN);
        sendResponse(game.getWaitingPlayer(), res);
    }

    private void updateCard(GameInstance game, ArrayList<Integer> cardIndice, int revealTime) {
        ServerResponse res = ResponseCreator.createShowCard(cardIndice, revealTime);
        sendResponse(game.getPlayer1(), res);
        sendResponse(game.getPlayer2(), res);
    }

    private void updateFinished(GameInstance game) {
        ServerResponse res;
        if (game.getPlayer1().getScore() > game.getPlayer2().getScore()) {
            res = ResponseCreator.createFinish(game.getPlayer1().getClientId());
        }
        else if (game.getPlayer1().getScore() < game.getPlayer2().getScore()) {
            res = ResponseCreator.createFinish(game.getPlayer2().getClientId());
        }
        // tie
        else {
            res = ResponseCreator.createFinish(null);
        }
        sendResponse(game.getPlayer1(), res);
        sendResponse(game.getPlayer2(), res);
    }

    private void terminateGame(GameInstance game) {
        game.getPlayer1().setPlaying(false);
        game.getPlayer2().setPlaying(false);
        game.getPlayer1().setGameId(null);
        game.getPlayer2().setGameId(null);
        games.remove(game.getGameID());
    }

    // Each connection is run on a designated thread
    private class ConnectionThread implements Runnable {
        /**
         * The Connection.
         */
        Connection connection;
        /**
         * The Buffer in.
         */
        ObjectInputStream bufIn;
        /**
         * The Buffer out.
         */
        ObjectOutputStream bufOut;

        /**
         * Instantiates a new Connection thread.
         *
         * @param connection the connection
         */
        public ConnectionThread(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                // Create reader/writer
                bufOut = new ObjectOutputStream(new BufferedOutputStream(connection.getSocket().getOutputStream()));
                bufOut.flush();
                bufIn = new ObjectInputStream(new BufferedInputStream(connection.getSocket().getInputStream()));
                this.connection.setInputStream(bufIn);
                this.connection.setOutputStream(bufOut);
            } catch (SocketException e) {
                handleDisconnect();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            try {
                while (true) {
                    try {
                        ServerRequest req = (ServerRequest) bufIn.readObject();
                        requestDispatcher(req);
                    }
                    // Disconnection
                    catch (SocketException e) {
                        handleDisconnect();
                        return;
                    }

                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Remove connection
        private void handleDisconnect() {
            if (connection.isPlaying()) {
                // Get the game instance of the connection and terminate it
                GameInstance game = games.get(connection.getGameId());
                terminateGame(game);
                // Send a message to the other player
                if (connection == game.getPlayer1()) {
                    notifyDisconnect(game.getPlayer2());
                }
                //
                else {
                    notifyDisconnect(game.getPlayer1());
                }
            }
            try {
                connection.getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connections.remove(connection.getClientId());
            System.out.println("Disconnection. Total: " + connections.size());
        }

        private void requestDispatcher(ServerRequest req) {
            switch(req.getType()) {
                case JOIN:
                    joinRequest(req.getIndex());
                    break;
                case CLICK:
                    clickRequest(req);
                    break;
                default: break;
            }
        }

        private void joinRequest(int boardSize) {
            if (connection.isPlaying()) {
                // Don't respond to join request if the player is in game, as it makes no sense
                return;
            }
            // Check validity of boardSize passed
            if (boardSize > Constants.UNIQUE_CARDS*2 || boardSize % 2 != 0) {
                return;
            }
            // If boardSize <= 0, set to default boardSize
            if (boardSize <= 0) {
                boardSize = defaultBoardSize;
            }
            addWaitingPlayer(this.connection, boardSize);
        }

        private void clickRequest(ServerRequest req) {
            // Get the game instance this connection is in
            GameInstance game = games.get(connection.getGameId());
            // Make sure the client is in game
            if (game == null) {
                return;
            }
            // Check if it's the player's turn, if not then discard request
            if (!game.getPlayingPlayer().equals(connection)) {
                return;
            }
            // Check for valid card index
            if (req.getIndex() < 0 || req.getIndex() >= game.getCardsOrder().size()
                    || game.getFirstCardIndex() == req.getIndex()) {
                return;
            }

            ArrayList<Integer> indices = new ArrayList<>();
            if (game.getFirstCardIndex() == -1) {
                // This is the first selected card this turn, so just show it until the second one is chosen
                game.setFirstCardIndex(req.getIndex());
                indices.add(req.getIndex());
                updateCard(game, indices, Constants.CARD_REVEAL_INDEFINITELY);
            }
            else {
                // Cards match, add point and keep them revealed
                if (game.getCardsOrder().get(game.getFirstCardIndex()).equals(game.getCardsOrder().get(req.getIndex()))) {
                    indices.add(req.getIndex());
                    updateCard(game, indices, Constants.CARD_REVEAL_INDEFINITELY);
                    connection.addPoint();
                    updateScore(game);
                    // Check if all cards are revealed
                    if (game.getPlayer1().getScore() + game.getPlayer2().getScore() ==
                            game.getCardsOrder().size()/2) {
                        updateFinished(game);
                        terminateGame(game);
                    }
                }
                // Cards don't match, hide them
                else {
                    indices.add(req.getIndex());
                    indices.add(game.getFirstCardIndex());
                    updateCard(game, indices, CARD_REVEAL_TIME);
                    try {
                        Thread.sleep(CARD_REVEAL_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                game.setFirstCardIndex(-1);
                game.switchPlayers();
                updateTurns(game);
            }
        }

        // Notify the connection that the other side had disconnected
        private void notifyDisconnect(Connection connection) {
            ServerResponse res = ResponseCreator.createDisconnect();
            sendResponse(connection, res);
        }
    }
}
