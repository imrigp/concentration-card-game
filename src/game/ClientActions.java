package game;
/*
Author: Imri
Date: 2020-02-01
*/

/**
 * The interface Client actions.
 * This is the controller whose interacting with the server.
 */
public interface ClientActions {
    /**
     * Connect.
     */
    void connect(String host, int port);

    /**
     * Disconnect.
     */
    void disconnect();

    /**
     * Find opponent.
     *
     * @param size the size
     */
    void findOpponent(int size); // Amount of cards

    /**
     * Card clicked.
     *
     * @param card the card
     */
    void cardClicked(Card card);
}
