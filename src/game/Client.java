package game;
/*
Author: Imri
Date: 2020-02-01
*/

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * The type Client.
 */
public class Client implements ClientActions {

    private GameView view;
    private Socket connection;
    private ObjectInputStream bufIn;
    private ObjectOutputStream bufOut;
    private ArrayList<Integer> cardsOrder;
    private UUID clientId; // ID the server assigns the client

    /**
     * Instantiates a new Client.
     *
     * @param view the view
     */
    public Client(GameView view) {
        this.view = view;
    }

    private void sendRequest(ServerRequest req) throws IOException {
        bufOut.writeObject(req);
        bufOut.flush();
    }

    @Override
    public void connect(final String host, final int port) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    view.waitConnection();
                    connection = new Socket(host, port);
                    // Create reader/writer
                    try {
                        bufOut = new ObjectOutputStream(new BufferedOutputStream(connection.getOutputStream()));
                        bufOut.flush();
                        bufIn = new ObjectInputStream(new BufferedInputStream(connection.getInputStream()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    view.connected();

                    while (true) {
                        try {
                            final ServerResponse res = (ServerResponse) bufIn.readObject();
                            EventQueue.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    responseDispatcher(res);
                                }
                            });
                        }
                        // Disconnection
                        catch (SocketException e) {
                            view.connectionError(e.getMessage(), true);
                            return;
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                } catch (IOException e) {
                    view.connectionError(e.getMessage(), true);
                }
            }
        }).start();
    }

    private void responseDispatcher(ServerResponse res) {
        // Set client ID
        if (res.getClientId() != null && !res.getClientId().equals(this.clientId)) {
            this.clientId = res.getClientId();
        }
        switch (res.getResponseType()) {
            case JOIN:
                view.setCardOrder(res.getCardsOrder());
                break;
            case PLAYER_TURN:
                view.setPlayerTurn();
                break;
            case OPPONENT_TURN:
                view.setOpponentTurn();
                break;
            case SET_SCORE:
                view.setPlayerScore(res.getPlayerScore());
                view.setOpponentScore(res.getOpponentScore());
                break;
            case SHOW_CARD:
                showCards(res.getCardIndice(), res.getRevealTimeMS());
                break;
            case FINISH:
                // tie
                if (res.getPlayerWon() == null) {
                    view.declareWinner(0);
                }
                // win
                else if (res.getPlayerWon().equals(clientId)) {
                    view.declareWinner(1);
                }
                // lose
                else {
                    view.declareWinner(-1);
                }
                break;
            case DISCONNECT:
                view.opponentDisconnected();
                break;
            default: break;
        }
    }

    private void showCards(final ArrayList<Integer> indice, final int revealTime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                view.showCards(indice);
                if (revealTime != Constants.CARD_REVEAL_INDEFINITELY) {
                    try {
                        Thread.sleep(revealTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    view.hideCards(indice);
                }
            }
        }).start();
    }

    @Override
    public void disconnect() {
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void findOpponent(final int size) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerRequest req = new ServerRequest(ServerRequest.RequestType.JOIN, size);
                    sendRequest(req);
                    view.waitOpponent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void cardClicked(final Card card) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerRequest req = new ServerRequest(ServerRequest.RequestType.CLICK, card.getIndex());
                    req.setClientId(clientId);
                    sendRequest(req);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
