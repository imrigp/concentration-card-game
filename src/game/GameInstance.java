package game;
/*
Author: Imri
Date: 2020-02-01
*/

import java.util.ArrayList;
import java.util.UUID;

/**
 * The type Game instance.
 */
public class GameInstance {
    private UUID gameID;
    private Connection player1;
    private Connection player2;
    private ArrayList<Integer> cardsOrder;
    private Connection playingPlayer; // Who's turn it is now
    private Connection waitingPlayer; // The player waiting for his turn
    private int firstCardIndex; // The id of the first card revealed this turn, or -1 if no card has been revealed yet.

    /**
     * Instantiates a new Game instance.
     *
     * @param gameID     the game id
     * @param player1    the player 1
     * @param player2    the player 2
     * @param cardsOrder the cards order
     */
    public GameInstance(UUID gameID, Connection player1, Connection player2, ArrayList<Integer> cardsOrder) {
        this.gameID = gameID;
        this.player1 = player1;
        this.player2 = player2;
        this.cardsOrder = cardsOrder;
        this.playingPlayer = null;
        this.waitingPlayer = null;
        this.firstCardIndex = -1;
    }

    /**
     * Gets game id.
     *
     * @return the game id
     */
    public UUID getGameID() {
        return gameID;
    }

    /**
     * Gets player 1.
     *
     * @return the player 1
     */
    public Connection getPlayer1() {
        return player1;
    }

    /**
     * Gets player 2.
     *
     * @return the player 2
     */
    public Connection getPlayer2() {
        return player2;
    }

    /**
     * Gets cards order.
     *
     * @return the cards order
     */
    public ArrayList<Integer> getCardsOrder() {
        return cardsOrder;
    }

    /**
     * Gets playing player.
     *
     * @return the playing player
     */
    public Connection getPlayingPlayer() {
        return playingPlayer;
    }

    /**
     * Sets playing player.
     *
     * @param playingPlayer the playing player
     */
    public void setPlayingPlayer(Connection playingPlayer) {
        this.playingPlayer = playingPlayer;
    }

    /**
     * Gets waiting player.
     *
     * @return the waiting player
     */
    public Connection getWaitingPlayer() {
        return waitingPlayer;
    }

    /**
     * Sets waiting player.
     *
     * @param waitingPlayer the waiting player
     */
    public void setWaitingPlayer(Connection waitingPlayer) {
        this.waitingPlayer = waitingPlayer;
    }

    /**
     * Gets first card index.
     *
     * @return the first card index
     */
    public int getFirstCardIndex() {
        return firstCardIndex;
    }

    /**
     * Sets first card index.
     *
     * @param firstCardIndex the first card index
     */
    public void setFirstCardIndex(int firstCardIndex) {
        this.firstCardIndex = firstCardIndex;
    }

    /**
     * Switch players.
     */
    public void switchPlayers() {
        Connection tmp = playingPlayer;
        playingPlayer = waitingPlayer;
        waitingPlayer = tmp;
    }

}
