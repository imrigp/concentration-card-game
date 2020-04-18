package game;
/*
Author: Imri
Date: 2020-02-01
*/

import java.io.Serializable;
import java.util.UUID;

/**
 * The type Server request.
 */
public class ServerRequest implements Serializable {
    /**
     * The enum Request type.
     */
    public enum RequestType {
        /**
         * Click request type.
         */
        CLICK,
        /**
         * Join request type.
         */
        JOIN}

    private UUID clientId;
    private RequestType type;
    private int index; // Holds the card index clicked for CLICK request, and number of cards to play for JOIN request

    /**
     * Instantiates a new Server request.
     *
     * @param type  the type
     * @param index the index
     */
    public ServerRequest(RequestType type, int index) {
        this.type = type;
        this.index = index;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public RequestType getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(RequestType type) {
        this.type = type;
    }

    /**
     * Gets index.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets index.
     *
     * @param index the index
     */
    public void setIndex(int index) {
        this.index = index;
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
