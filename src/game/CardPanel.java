package game;
/*
Author: Imri
Date: 2020-02-01
*/

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The type Card panel.
 */
public class CardPanel extends JPanel {
    private static final int CARDS_PER_ROW = 6;
    private static final int TOTAL_CARD_CELLS = Constants.UNIQUE_CARDS * 2;

    private MouseListener listener;
    private Card[] template_cards;
    private ArrayList<Card> gameCards;

    /**
     * Instantiates a new Card panel.
     *
     * @param mouseListener the mouse listener
     * @throws MissingResourceException the missing resource exception
     * @throws IOException              the io exception
     */
    public CardPanel(MouseListener mouseListener) throws MissingResourceException, IOException {
        this.setLayout(new GridLayout(0, CARDS_PER_ROW, 10, 10));
        this.template_cards = new Card[Constants.UNIQUE_CARDS];
        this.listener = mouseListener;


        loadTemplateCards();
    }

    /**
     * Show card.
     *
     * @param index the index
     */
    public void showCard(int index) {
        gameCards.get(index).showCard();
    }

    /**
     * Hide card.
     *
     * @param index the index
     */
    public void hideCard(int index) {
        gameCards.get(index).hideCard();
    }

    /**
     * Reveals all cards.
     */
    public void showAll() {
        for (Card card : template_cards) {
            card.showCard();
        }
    }

    /**
     * fills the panel with card indices.
     *
     * @param permutation the permutation
     */
    public void setOrder(ArrayList<Integer> permutation) {
        gameCards = new ArrayList<>(permutation.size());
        this.removeAll();
        // Fill cards
        for (int i = 0; i < permutation.size(); i++) {
            addCard(i, permutation.get(i));
        }
        // Fill empty cells with empty component (for uniform board overall)
        for (int i = 0; i < TOTAL_CARD_CELLS - permutation.size(); i++) {
           this.add(new JLabel());
        }
        this.revalidate();
        this.repaint();
    }

    /**
     * Clears the board.
     */
    public void clear() {
        this.removeAll();
        this.revalidate();
        this.repaint();
    }

    private void addCard(int index, int id) {
        Card card = new Card(template_cards[id]);
        card.setIndex(index);
        this.add(card);
        card.addMouseListener(this.listener);
        gameCards.add(card);
    }

    // Get cards from resource folder
    private void loadTemplateCards() throws IOException, MissingResourceException {
        for (int id = 0; id < Constants.UNIQUE_CARDS; id++) {
            File cardFile = Utils.getResource(getClass(),"cards/card" + id + ".png");
            template_cards[id] = new Card(ImageIO.read(cardFile), id);
        }
    }

}
