package game;
/*
Author: Imri
Date: 2020-02-01
*/

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

/**
 * The type Connection.
 */
public class Connection {
    private Socket socket;
    private UUID clientId;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private UUID gameId;
    private int score;
    private boolean isPlaying;

    /**
     * Instantiates a new Connection.
     *
     * @param clientId the client id
     * @param socket   the socket
     */
    public Connection(UUID clientId, Socket socket) {
        this.socket = socket;
        this.clientId = clientId;
        this.isPlaying = false;
    }

    /**
     * Gets socket.
     *
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
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
     * Gets input stream.
     *
     * @return the input stream
     */
    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    /**
     * Gets output stream.
     *
     * @return the output stream
     */
    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Sets input stream.
     *
     * @param inputStream the input stream
     */
    public void setInputStream(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Sets output stream.
     *
     * @param outputStream the output stream
     */
    public void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Is playing boolean.
     *
     * @return the boolean
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Sets playing.
     *
     * @param playing the playing
     */
    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    /**
     * Gets game id.
     *
     * @return the game id
     */
    public UUID getGameId() {
        return gameId;
    }

    /**
     * Sets game id.
     *
     * @param gameId the game id
     */
    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets score.
     *
     * @param score the score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Add point.
     */
    public void addPoint() {
        this.score++;
    }

    @Override
    public boolean equals(Object obj) {
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        return getClientId() == ((Connection)obj).getClientId();
    }
}
