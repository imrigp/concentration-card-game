package game;
/*
Author: Imri
Date: 2020-02-01
NOTE: Make sure the server is running before starting a client.
*/

import javax.swing.*;

public class MainClient {
    public static void main(String[] args) {
        startClient();
    }

    public static void startClient() {
        ViewWindow view = new ViewWindow();
        Client client = new Client(view);
        view.setClient(client);
        JFrame frame = new JFrame("Client");
        frame.add(view);
        frame.setSize(1100, 1200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
