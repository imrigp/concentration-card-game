package game;
/*
Author: Imri
Date: 2020-02-01
*/

import java.util.ArrayList;

/**
 * The interface Game view.
 * Each game view should implement this, so the controller could interact with it.
 */
public interface GameView {
    /**
     * Wait connection.
     */
    void waitConnection(); // Waiting to connect the server

    /**
     * Connected.
     */
    void connected(); // Connection to server succeeded

    /**
     * Connection error.
     *
     * @param msg       the msg
     * @param terminate the terminate
     */
    void connectionError(String msg, boolean terminate); // Connection to server failed

    /**
     * Wait opponent.
     */
    void waitOpponent(); // Connected, waiting for opponent

    /**
     * Disconnected.
     */
    void disconnected(); // Disconnected from server

    /**
     * Declare winner.
     *
     * @param playerWon the player won
     */
    void declareWinner(int playerWon); // Match finished, -1 lose, 0 tie and 1 win

    /**
     * Opponent disconnected.
     */
    void opponentDisconnected(); // Match finished due to opponent disconnection

    /**
     * Sets player turn.
     */
    void setPlayerTurn(); // Player's turn to play

    /**
     * Sets opponent turn.
     */
    void setOpponentTurn(); // Opponent's turn to play

    /**
     * Sets player score.
     *
     * @param score the score
     */
    void setPlayerScore(int score); // Set player's score

    /**
     * Sets opponent score.
     *
     * @param score the score
     */
    void setOpponentScore(int score); // Set opponent's score

    /**
     * Sets card order.
     *
     * @param order the order
     */
    void setCardOrder(ArrayList<Integer> order); // Arrange the cards in the order specified (each cell is an index of a card)

    /**
     * Show cards.
     *
     * @param cards the cards
     */
    void showCards(ArrayList<Integer> cards); // Show the cards specified (each cell is an index of a card to show)

    /**
     * Hide cards.
     *
     * @param cards the cards
     */
    void hideCards(ArrayList<Integer> cards); // Hide the cards specified (each cell is an index of a card to hide)

}
