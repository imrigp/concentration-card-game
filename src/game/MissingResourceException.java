package game;
/*
Author: Imri
Date: 2020-02-01
*/

public class MissingResourceException extends Exception {
    MissingResourceException(String resource) {
        super("Missing '" + resource + "' resource.");
    }
}
