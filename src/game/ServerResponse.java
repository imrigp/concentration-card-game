package game;
/*
Author: Imri
Date: 2020-02-01
*/

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * The type Server response.
 */
public class ServerResponse implements Serializable {
    /**
     * The enum Response type.
     */
    public enum ResponseType {
        /**
         * Join response type.
         */
        JOIN,
        /**
         * Show card response type.
         */
        SHOW_CARD,
        /**
         * Finish response type.
         */
        FINISH,
        /**
         * Player turn response type.
         */
        PLAYER_TURN,
        /**
         * Opponent turn response type.
         */
        OPPONENT_TURN,
        /**
         * Set score response type.
         */
        SET_SCORE,
        /**
         * Disconnect response type.
         */
        DISCONNECT}

    private ResponseType type;
    private UUID clientId;
    private ArrayList<Integer> cardsOrder;
    private ArrayList<Integer> cardIndice;
    private int revealTimeMS; // How long to reveal the card, in milliseconds. -1 for indefinitely
    private UUID playerWon;
    private int playerScore;
    private int opponentScore;

    /**
     * Instantiates a new Server response.
     *
     * @param type the type
     */
    public ServerResponse(ResponseType type) {
        this.type = type;
    }

    /**
     * Gets response type.
     *
     * @return the response type
     */
    public ResponseType getResponseType() {
        return type;
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
     * Sets cards order.
     *
     * @param cardsOrder the cards order
     */
    public void setCardsOrder(ArrayList<Integer> cardsOrder) {
        this.cardsOrder = cardsOrder;
    }

    /**
     * Gets card indice.
     *
     * @return the card indice
     */
    public ArrayList<Integer> getCardIndice() {
        return cardIndice;
    }

    /**
     * Sets card indice.
     *
     * @param cardIndice the card indice
     */
    public void setCardIndice(ArrayList<Integer> cardIndice) {
        this.cardIndice = cardIndice;
    }

    /**
     * Gets reveal time ms.
     *
     * @return the reveal time ms
     */
    public int getRevealTimeMS() {
        return revealTimeMS;
    }

    /**
     * Sets reveal time ms.
     *
     * @param revealTimeMS the reveal time ms
     */
    public void setRevealTimeMS(int revealTimeMS) {
        this.revealTimeMS = revealTimeMS;
    }

    /**
     * Gets player won.
     *
     * @return the player won
     */
    public UUID getPlayerWon() {
        return playerWon;
    }

    /**
     * Sets player won.
     *
     * @param playerWon the player won
     */
    public void setPlayerWon(UUID playerWon) {
        this.playerWon = playerWon;
    }

    /**
     * Gets player score.
     *
     * @return the player score
     */
    public int getPlayerScore() {
        return playerScore;
    }

    /**
     * Sets player score.
     *
     * @param playerScore the player score
     */
    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    /**
     * Gets opponent score.
     *
     * @return the opponent score
     */
    public int getOpponentScore() {
        return opponentScore;
    }

    /**
     * Sets opponent score.
     *
     * @param opponentScore the opponent score
     */
    public void setOpponentScore(int opponentScore) {
        this.opponentScore = opponentScore;
    }

    /**
     * Gets client id.
     *
     * @return the client id
     */
    public UUID getClientId() {
        return clientId;
    }

    /**
     * Sets client id.
     *
     * @param clientId the client id
     */
    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }
}