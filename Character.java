import java.io.*;

import java.awt.*;

/**
 * The object the user can control to navigate the maze
 *
 * Methods: 
 *          draw(): creates a red circle (character art)
 *
 * @author Amar and Avi Ruthen
 * @version May 22, 2020
 */
public class Character extends AnimateObject implements Serializable {
    // instance variables - replace the example below with your own

    /**
     * Initializes Character object
     * 
     * @param squareSize the pixel-length of each side of a block
     * @param gridHeight the number of blocks per column
     * @param gridWidth  the number of blocks per row
     */
    public Character(int squareSize, int gridHeight, int gridWidth) {
        super(squareSize, gridHeight, gridWidth);
        xBlock = sizeAdj;
        yBlock = gridHeight * squareSize - (3 * sizeAdj);
        moves = 0;
    }

    /**
     * Draws a red oval (represents character)
     * 
     * @param g The Graphics object for drawing on JFrame
     */
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(xBlock, yBlock, squareSize / 2, squareSize / 2);
    }
}