package game;
/*
Author: Imri
Date: 2020-02-01
*/

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * The type Utils.
 */
public class Utils {

    private Utils() {}

    /**
     * Gets resource.
     *
     * @param c    the c
     * @param name the name
     * @return the resource
     * @throws MissingResourceException the missing resource exception
     */
    static File getResource(Class c, String name) throws MissingResourceException {
        URL url = c.getClassLoader().getResource(name);
        if (url == null) {
            throw new MissingResourceException(name);
        }
        return new File(url.getFile());
    }

    /**
     * Create icon image icon.
     *
     * @param c        the c
     * @param resource the resource
     * @return the image icon
     * @throws IOException              the io exception
     * @throws MissingResourceException the missing resource exception
     */
    static ImageIcon createIcon(Class c, String resource) throws IOException, MissingResourceException {
        return createIcon(getResource(c, resource));
    }

    /**
     * Create icon image icon.
     *
     * @param file the file
     * @return the image icon
     * @throws IOException the io exception
     */
    static ImageIcon createIcon(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        return new ImageIcon(image);
    }
}
