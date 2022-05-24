import java.io.*;
import java.awt.*;

/**
 * Defines and draws a Chaser object, which randomly traverses maze
 *      and will reset character position when it is in contact with it
 *      
 * Methods:
 *          draw(): creates a blue circle (chaser art)     
 *
 * @author Amar and Avi Ruthen
 * @version May 22, 2020
 */
public class Chaser extends AnimateObject implements Serializable {
    // instance variables - replace the example below with your own
    boolean hideChaser = false;
    public Chaser(int squareSize, int gridHeight, int gridWidth) {
        super(squareSize, gridHeight, gridWidth);
        xBlock = gridWidth*squareSize - (3 * sizeAdj);
        yBlock = sizeAdj;
    }
    
    public Chaser(int squareSize, int gridHeight, int gridWidth, String difficulty) {
        super (squareSize, gridHeight, gridWidth);
        if(difficulty.equals("Easy"))
            xBlock = gridWidth*squareSize - (3 * sizeAdj) - 3;
        else
            xBlock = gridWidth*squareSize - (3 * sizeAdj);
        yBlock = sizeAdj;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillOval(xBlock, yBlock, squareSize / 2, squareSize / 2);
        //System.out.println((xBlock - (squareSize/4)) % squareSize + ", " + (yBlock - (squareSize/4)) % squareSize);
        //System.out.println(squareSize);
    }
}