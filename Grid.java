import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;
import java.awt.geom.*;

/**
 * The code for the grid
 * 
 * Methods: 
 *          draw(g): draws maze grid
 * 
 * @Avi and Amar Ruthen @12.22.2020
 */
public class Grid {
    int xLoc, yLoc, squareSize, width, height;
    int gridLength;

    /**
     * Constructor for the Grid
     */
    public Grid(int xLoc, int yLoc, int squareSize, int width, int height) {
        this.xLoc = xLoc;
        this.yLoc = yLoc;
        this.squareSize = squareSize;
        this.width = width;
        this.height = height;
    }

    /**
     * Establishes a full grid for the maze 
     * Lines are later redrawn (and therefore erased) with a white line
     *    if they are considered an "open" direction to travel
     * 
     * @param g allows for drawing to be displayed on Graphics Window
     */
    public void draw(Graphics g) {
        // draw the grid:
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                // set location of where to draw current rectangle
                int x = (int) (squareSize * col) + (squareSize * xLoc);
                int y = (int) (squareSize * row) + (squareSize * yLoc);

                g.setColor(Color.BLACK);
                g.drawRect(x, y, squareSize, squareSize);
            }
        }
    }
}