package game;
/*
Author: Imri
Date: 2020-02-01
*/

public final class Constants {
    public static final int ERROR_FLAG = -1;
    public static final int UNIQUE_CARDS = 18; // Number of card images
    public static final int SERVER_PORT = 6666;
    public static final int CARD_REVEAL_INDEFINITELY = -1;

    // Shouldn't be called
    private Constants () {
        throw new AssertionError();
    }
}
