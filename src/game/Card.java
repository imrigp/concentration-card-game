package game;
/*
Author: Imri
Date: 2020-02-01
*/

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * The type Card.
 */
public class Card extends JPanel {
    private static final String CARD_BACK_FILE = "imgs/card_back.png";
    private static BufferedImage CARD_BACK_IMG;

    private static final float CARD_BORDER_THICKNESS  = 40.0f;
    private static final Color CARD_BORDER_COLOR = Color.WHITE;

    private int id; // duplicates will have the same id
    private int index; // location of the card, -1 for unplaced cards
    private BufferedImage img;
    private BufferedImage cardBackImg;
    private boolean isShown;

    /**
     * Instantiates a new Card.
     *
     * @param img the img
     * @param id  the id
     * @throws IOException              the io exception
     * @throws MissingResourceException the missing resource exception
     */
    public Card(BufferedImage img, int id) throws IOException, MissingResourceException {
        this.setBackground(CARD_BORDER_COLOR);
        this.index = -1;
        this.id = id;
        this.img = img;
        this.cardBackImg = getCardBackImage();
        this.isShown = false;
    }

    /**
     * Instantiates a new Card.
     *
     * @param other the other
     */
// Copy constructor
    public Card(Card other) {
        super();
        this.setBackground(other.getBackground());
        this.id = other.id;
        this.img = other.img;
        this.cardBackImg = other.cardBackImg;
        this.isShown = other.isShown;
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
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Is shown boolean.
     *
     * @return the boolean
     */
    public boolean isShown() {
        return isShown;
    }

    /**
     * Show card.
     */
    public void showCard() {
        if (!this.isShown) {
            this.isShown = true;
            repaint();
        }
    }

    /**
     * Hide card.
     */
    public void hideCard() {
        if (this.isShown) {
            this.isShown = false;
            repaint();
        }
    }

    /**
     * Flip.
     */
    public void flip() {
        this.isShown = !this.isShown;
        repaint();
    }

    private Image getScaledImage(BufferedImage img) {
        return img.getScaledInstance(
                (int)(this.getWidth()-CARD_BORDER_THICKNESS),
                (int)(this.getHeight()-CARD_BORDER_THICKNESS),
                Image.SCALE_SMOOTH);
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        // Draw the image, filling the whole panel
        if (this.isShown) {
            Image scaledImg = getScaledImage(this.img);
            g.drawImage(scaledImg,
                    (int)CARD_BORDER_THICKNESS/2,
                    (int)CARD_BORDER_THICKNESS/2,
                    scaledImg.getWidth(null),
                    scaledImg.getHeight(null),
                    null);
        }
        else {
            g.drawImage(getScaledImage(this.cardBackImg), 0, 0, this.getWidth(), this.getHeight(), null);
        }
    }

    private static BufferedImage getCardBackImage() throws IOException, MissingResourceException {
        if (CARD_BACK_IMG == null) {
            CARD_BACK_IMG = ImageIO.read(Utils.getResource(Card.class, CARD_BACK_FILE));
        }
        return CARD_BACK_IMG;
    }
}
