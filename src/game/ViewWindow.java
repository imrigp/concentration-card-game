package game;
/*
Author: Imri
Date: 2020-02-01
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The type View window.
 */
public class ViewWindow extends JPanel implements MouseListener, ActionListener, GameView {
    private static final float LBL_PLAYER_FONT_SIZE = 35.0f;
    private static final float LBL_SCORE_FONT_SIZE = 20.0f;
    private static final float LBL_STATUS_FONT_SIZE = 18.0f;
    private static final int STATUS_BORDER_MARGIN = 20;
    private static final int STATUS_BORDER_THICKNESS = 5;
    private static final int STATUS_EMPTY_BORDER_THICKNESS = STATUS_BORDER_MARGIN + STATUS_BORDER_THICKNESS;
    private static final Color STATUS_BORDER_COLOR = Color.BLUE;
    private static final String BTN_FIND_OPPONENT_TEXT  = "Find Opponent";
    private static final String LBL_PLAYER_TEXT = "YOU";
    private static final String LBL_OPPONENT_TEXT  = "OPPONENT";
    private static final String LBL_SCORE_TEXT  = "Score: ";
    private static final String LBL_OP_TURN_TEXT  = "Opponent turn...";
    private static final String LBL_PLAYER_TURN_TEXT  = "Your turn!";
    private static final String LBL_NO_GAME_TEXT  = "No game. Press '" + BTN_FIND_OPPONENT_TEXT + "' to play";
    private static final String LBL_CONNECTING_TEXT  = "Connecting...";
    private static final String LBL_WAITING_TEXT  = "Waiting for opponent...";
    private static final String WINNER_TITLE_TEXT  = "Win";
    private static final String LOSER_TITLE_TEXT  = "Lose";
    private static final String TIE_TITLE_TEXT  = "Tie";
    private static final String MSG_WINNER_TEXT = "You win!<br>Your opponent had no chance, find a worthy one next time.";
    private static final String MSG_LOSER_TEXT = "You lose.<br>Perhaps start playing games you are good at...";
    private static final String MSG_TIE_TEXT = "It's a tie!.<br>Lucky you... You couldn't ask for more. We'll count that as a lose.";
    private static final String MSG_OPPONENT_DISCONNECTED_TEXT = "Your opponent disconnected.\nIt seems he had foreseen his demise...";
    private static final String LBL_HOST_TEXT = "Host:";
    private static final String LBL_PORT_TEXT = "Port:";
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "6666";
    private static final String BTN_CONNECT_TEXT = "Connect";

    private static ImageIcon ICON_WINNER;
    private static ImageIcon ICON_LOSER;


    private ClientActions client;
    private CardPanel cardPanel;
    private JPanel statusPanel;
    private JLabel lblOpponentScore;
    private JLabel lblPlayerScore;
    private JLabel lblGameStatus;
    private JPanel playerStatus;
    private JPanel opponentStatus;
    private JButton btnFindOpponent;
    private JLabel lblHost;
    private JLabel lblPort;
    private JTextField txtHost;
    private JTextField txtPort;
    private JButton btnConnect;

    /**
     * Instantiates a new View window.
     */
    public ViewWindow() {
        this.client = null;
        this.setLayout(new BorderLayout());
        try {
            loadResources();
            cardPanel = new CardPanel(this);
        }
        catch (MissingResourceException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }

        JLabel lblPlayer1 = new JLabel(LBL_PLAYER_TEXT);
        JLabel lblPlayer2 = new JLabel(LBL_OPPONENT_TEXT);
        lblPlayer1.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPlayer2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel score1 = new JLabel(LBL_SCORE_TEXT);
        playerStatus = new JPanel();
        playerStatus.setLayout(new BoxLayout(playerStatus, BoxLayout.Y_AXIS));
        playerStatus.add(lblPlayer1);
        lblPlayerScore = new JLabel();
        lblPlayerScore.setAlignmentX(Component.CENTER_ALIGNMENT);
        score1.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel scoreP1 = new JPanel();
        scoreP1.add(score1);
        scoreP1.add(lblPlayerScore);
        playerStatus.add(scoreP1);

        JLabel score2 = new JLabel(LBL_SCORE_TEXT);
        opponentStatus = new JPanel();
        opponentStatus.setLayout(new BoxLayout(opponentStatus, BoxLayout.Y_AXIS));
        opponentStatus.add(lblPlayer2);
        lblOpponentScore = new JLabel();
        lblOpponentScore.setAlignmentX(Component.CENTER_ALIGNMENT);
        score2.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel scoreP2 = new JPanel();
        scoreP2.add(score2);
        scoreP2.add(lblOpponentScore);
        opponentStatus.add(scoreP2);

        lblGameStatus = new JLabel();
        statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusPanel.add(Box.createHorizontalGlue());
        statusPanel.add(Box.createHorizontalGlue());
        statusPanel.add(playerStatus);
        statusPanel.add(Box.createHorizontalGlue());
        statusPanel.add(lblGameStatus);
        statusPanel.add(Box.createHorizontalGlue());
        statusPanel.add(opponentStatus);
        statusPanel.add(Box.createHorizontalGlue());
        statusPanel.add(Box.createHorizontalGlue());

        lblPlayer1.setFont(lblPlayer1.getFont().deriveFont(LBL_PLAYER_FONT_SIZE));
        lblPlayer2.setFont(lblPlayer2.getFont().deriveFont(LBL_PLAYER_FONT_SIZE));
        lblOpponentScore.setFont(lblOpponentScore.getFont().deriveFont(LBL_SCORE_FONT_SIZE));
        lblPlayerScore.setFont(lblPlayerScore.getFont().deriveFont(LBL_SCORE_FONT_SIZE));
        lblGameStatus.setFont(lblGameStatus.getFont().deriveFont(LBL_STATUS_FONT_SIZE));
        score1.setFont(lblGameStatus.getFont().deriveFont(LBL_STATUS_FONT_SIZE));
        score2.setFont(lblGameStatus.getFont().deriveFont(LBL_STATUS_FONT_SIZE));

        btnFindOpponent = new JButton(BTN_FIND_OPPONENT_TEXT);
        btnFindOpponent.addActionListener(this);
        btnFindOpponent.setVisible(false);

        lblHost = new JLabel(LBL_HOST_TEXT);
        lblPort = new JLabel(LBL_PORT_TEXT);
        txtHost = new JTextField(DEFAULT_HOST);
        txtHost.setColumns(10);
        txtPort = new JTextField(DEFAULT_PORT);
        txtPort.setColumns(10);

        btnConnect = new JButton(BTN_CONNECT_TEXT);
        btnConnect.addActionListener(this);
        JPanel connectionPanel = new JPanel();
        connectionPanel.add(lblHost);
        connectionPanel.add(txtHost);
        connectionPanel.add(lblPort);
        connectionPanel.add(txtPort);
        connectionPanel.add(btnConnect);
        connectionPanel.add(btnFindOpponent);

        this.add(statusPanel, BorderLayout.NORTH);
        this.add(cardPanel, BorderLayout.CENTER);
        this.add(connectionPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets client.
     *
     * @param client the client
     */
    public void setClient(ClientActions client) {
        if (this.client != null) {
            throw new IllegalArgumentException("Can't override client.");
        }
        this.client = client;
    }

    public void declareWinner(int playerWon) {
        String title;
        StringBuilder msg = new StringBuilder();
        ImageIcon icon;

        msg.append("<html><body>"); // Using html because double \n creates too much space
        if (playerWon == 1) {
            title = WINNER_TITLE_TEXT;
            msg.append(MSG_WINNER_TEXT);
            icon = ICON_WINNER;
        }
        else if (playerWon == -1) {
            title = LOSER_TITLE_TEXT;
            msg.append(MSG_LOSER_TEXT);
            icon = ICON_LOSER;
        }
        // tie
        else {
            title = TIE_TITLE_TEXT;
            msg.append(MSG_TIE_TEXT);
            icon = ICON_LOSER;
        }
        String tab = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"; // yeah...
        msg.append("<br><br><br>").append(tab).append(LBL_SCORE_TEXT).append("<br>")
                .append(LBL_PLAYER_TEXT)
                .append(": ").append(lblPlayerScore.getText()).append(tab)
                .append(LBL_OPPONENT_TEXT)
                .append(": ").append(lblOpponentScore.getText())
                .append("</body></html>");
        JOptionPane.showMessageDialog(
                this, msg.toString(), title, JOptionPane.INFORMATION_MESSAGE, icon);
        setNoGameStatus();
    }

    @Override
    public void opponentDisconnected() {
        JOptionPane.showMessageDialog(
                this, MSG_OPPONENT_DISCONNECTED_TEXT, "", JOptionPane.INFORMATION_MESSAGE);
        setNoGameStatus();

    }

    public void setPlayerScore(int score) {
        lblPlayerScore.setText(String.valueOf(score));
    }

    public void setOpponentScore(int score) {
        lblOpponentScore.setText(String.valueOf(score));
    }

    public void setPlayerTurn() {
        lblGameStatus.setText(LBL_PLAYER_TURN_TEXT);
        setTurnBorder(playerStatus, opponentStatus);
    }

    public void setOpponentTurn() {
        lblGameStatus.setText(LBL_OP_TURN_TEXT);
        setTurnBorder(opponentStatus, playerStatus);
    }

    /**
     * Sets no game status.
     */
    public void setNoGameStatus() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                cardPanel.clear();
                playerStatus.setBorder(BorderFactory.createEmptyBorder(
                        STATUS_EMPTY_BORDER_THICKNESS, STATUS_EMPTY_BORDER_THICKNESS,
                        STATUS_EMPTY_BORDER_THICKNESS, STATUS_EMPTY_BORDER_THICKNESS));
                opponentStatus.setBorder(BorderFactory.createEmptyBorder(
                        STATUS_EMPTY_BORDER_THICKNESS, STATUS_EMPTY_BORDER_THICKNESS,
                        STATUS_EMPTY_BORDER_THICKNESS, STATUS_EMPTY_BORDER_THICKNESS));

                lblGameStatus.setText(LBL_NO_GAME_TEXT);
                btnFindOpponent.setEnabled(true);
            }
        });
    }

    /*Privates
    *
    *
    * */

    private void findOpponent() {
        String res = JOptionPane.showInputDialog(this,
                "Enter number of cards (0 to use server's default):");
        if (res == null) {
            return;
        }

        int boardSize;
        // Validate input
        try {
            boardSize = Integer.parseInt(res);
            if (boardSize % 2 != 0 || boardSize > Constants.UNIQUE_CARDS*2) {
                throw new Exception();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this, "Error: must be even number and up to " + Constants.UNIQUE_CARDS*2,
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        btnFindOpponent.setEnabled(false);
        client.findOpponent(boardSize);
    }

    // Validates port
    private int getPort() {
        int port;
        try {
            port = Integer.parseInt(txtPort.getText());
            if (port < 0 || port > 65535) {
                throw new Exception();
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this, "Port must be a number between 0-65535",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        return port;
    }

    private void setTurnBorder(JPanel playingTurn, JPanel waitingPlayer) {
        waitingPlayer.setBorder(BorderFactory.createEmptyBorder(
                STATUS_EMPTY_BORDER_THICKNESS, STATUS_EMPTY_BORDER_THICKNESS,
                STATUS_EMPTY_BORDER_THICKNESS, STATUS_EMPTY_BORDER_THICKNESS));
        playingTurn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(STATUS_BORDER_COLOR, STATUS_BORDER_THICKNESS),
                BorderFactory.createEmptyBorder(
                        STATUS_BORDER_MARGIN, STATUS_BORDER_MARGIN, STATUS_BORDER_MARGIN, STATUS_BORDER_MARGIN)));
    }

    private void loadResources() throws MissingResourceException, IOException {
        ICON_WINNER = Utils.createIcon(getClass(), "imgs/win_icon.png");
        ICON_LOSER = Utils.createIcon(getClass(), "imgs/lose_icon.png");
    }

    @Override public void mouseClicked(MouseEvent e) { }
    @Override
    public void mousePressed(MouseEvent e) {
        // Accept left clicks only
        if (e.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        // Must be CardImg, but check to be sure
        if (e.getSource() instanceof Card) {
            Card card = (Card)e.getSource();
            client.cardClicked(card);
        }
    }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case BTN_FIND_OPPONENT_TEXT:
                findOpponent();
                break;
            case BTN_CONNECT_TEXT:
                int port = getPort();
                if (port != -1) {
                    client.connect(txtHost.getText(), port);
                }
                break;
            default: break;
        }
    }

    @Override
    public void setCardOrder(ArrayList<Integer> order) {
        cardPanel.setOrder(order);
    }

    @Override
    public void showCards(ArrayList<Integer> cards) {
        for (Integer index : cards) {
            cardPanel.showCard(index);
        }
    }

    @Override
    public void hideCards(ArrayList<Integer> cards) {
        for (Integer index : cards) {
            cardPanel.hideCard(index);
        }
    }

    @Override
    public void waitConnection() {
        lblGameStatus.setText(LBL_CONNECTING_TEXT);
    }

    @Override
    public void connected() {
        lblGameStatus.setText(LBL_NO_GAME_TEXT);
        btnConnect.setVisible(false);
        txtHost.setVisible(false);
        txtPort.setVisible(false);
        lblHost.setVisible(false);
        lblPort.setVisible(false);
        btnFindOpponent.setVisible(true);
    }

    @Override
    public void connectionError(String msg, boolean terminate) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
        if (terminate) {
            System.exit(Constants.ERROR_FLAG);
        }
    }

    @Override
    public void waitOpponent() {
        lblGameStatus.setText(LBL_WAITING_TEXT);
    }

    @Override
    public void disconnected() {
        lblGameStatus.setText(LBL_NO_GAME_TEXT);
    }
}
