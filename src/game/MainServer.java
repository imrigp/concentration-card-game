package game;
/*
Author: Imri
Date: 2020-02-01
*/


import java.util.Scanner;

public class MainServer {
    private static final Scanner INPUT = new Scanner(System.in);

    public static void main(String[] args) {
        startServer();
    }

    private static void startServer() {
        int boardSize = askNumber("Enter default board size: ");
        while (boardSize % 2 != 0 || boardSize > Constants.UNIQUE_CARDS*2) {
            System.out.println("Error: must be even number and up to " + Constants.UNIQUE_CARDS*2);
            boardSize = askNumber("Enter default board size: ");
        }

        Server server = new Server(Constants.SERVER_PORT, boardSize);
        server.run();
    }

    // Asks for a positive number
    static private int askNumber(String msg) {
        int num;
        do {
            System.out.println(msg);
            while (!INPUT.hasNextInt()) {
                System.out.println("Error: must be an integer greater than 1.");
                System.out.println(msg);
                INPUT.next();
            }
            num = INPUT.nextInt();
            if (num < 1) {
                System.out.println("Error: must be an integer greater than 1.");
            }
        } while (num < 1);
        return num;
    }
}
