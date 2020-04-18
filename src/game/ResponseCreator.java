package game;
/*
Author: Imri
Date: 2020-02-01
*/

import java.util.ArrayList;
import java.util.UUID;

/**
 * The type Response creator.
 */
public class ResponseCreator {
    /**
     * Create join server response.
     *
     * @param cardsOrder the cards order
     * @return the server response
     */
    public static ServerResponse createJoin(ArrayList<Integer> cardsOrder) {
        ServerResponse res = new ServerResponse(ServerResponse.ResponseType.JOIN);
        res.setCardsOrder(cardsOrder);
        return res;
    }

    /**
     * Create show card server response.
     *
     * @param cardIndice the card indice
     * @param revealTime the reveal time
     * @return the server response
     */
    public static ServerResponse createShowCard(ArrayList<Integer> cardIndice, int revealTime) {
        ServerResponse res = new ServerResponse(ServerResponse.ResponseType.SHOW_CARD);
        res.setCardIndice(cardIndice);
        res.setRevealTimeMS(revealTime);
        return res;
    }

    /**
     * Create finish server response.
     *
     * @param playerWon the player won
     * @return the server response
     */
    public static ServerResponse createFinish(UUID playerWon) {
        ServerResponse res = new ServerResponse(ServerResponse.ResponseType.FINISH);
        res.setPlayerWon(playerWon);
        return res;
    }

    /**
     * Create player turn server response.
     *
     * @return the server response
     */
    public static ServerResponse createPlayerTurn() {
        return new ServerResponse(ServerResponse.ResponseType.PLAYER_TURN);
    }

    /**
     * Create opponent turn server response.
     *
     * @return the server response
     */
    public static ServerResponse createOpponentTurn() {
        return  new ServerResponse(ServerResponse.ResponseType.OPPONENT_TURN);
    }

    /**
     * Create set score server response.
     *
     * @return the server response
     */
    public static ServerResponse createSetScore() {
        return new ServerResponse(ServerResponse.ResponseType.SET_SCORE);
    }

    /**
     * Create disconnect server response.
     *
     * @return the server response
     */
    public static ServerResponse createDisconnect() {
        return new ServerResponse(ServerResponse.ResponseType.DISCONNECT);
    }
}
