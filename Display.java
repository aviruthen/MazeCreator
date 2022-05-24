import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.lang.Math;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.*;
import java.util.*;

/**
 * Arranges and draws all objects on the graphical interface
 * 
 * Methods: 
 *          saveProgress(): stores character location and maze generation
 *          loadProgress(): reimplements character location and maze generation
 * 
 * 
 * @author Avi and Amar Ruthen
 * @version 12.21.2020
 */
public class Display extends JPanel {

    Font myFont;
    int numSquaresAcross;
    int squareSize;
    int width, height;
    int portalCounter;
    Character c;
    Chaser ch;
    TelePortal tp;
    Grid grid;
    Maze maze;

    String charFileName = "character.ser";
    String mazeFileName = "maze.txt";

    boolean showSolution = false;

    /**
     * CONSTRUCTOR displays all objects created and the grid Certain powerups also
     * require the display
     */
    public Display(int w, int h, int numSquaresAcross, Grid g, Maze m, Character c, Chaser ch, TelePortal tp) {

        this.numSquaresAcross = numSquaresAcross;
        width = w;
        height = h;
        grid = g;
        maze = m;
        squareSize = 600 / numSquaresAcross;
        this.c = c;
        this.ch = ch;
        this.tp = tp;
        
        portalCounter = 0;

        this.myFont = new Font("Verdana", Font.BOLD, 16); // nice font for display purposes
        setPreferredSize(new Dimension(width, height)); // set size of display
    }

    /**
     * Repaints all objects on screen; called in Graphics Window by a timer calling
     * repaint()
     * 
     * @param g the graphics object, automatically sent when repaint() is called
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // clear the old drawings

        // Allows for smooth rotational graphics
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // creates background color of window the size of screen
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.BLACK);
        // Draw the grid and spawn areas (the green 5x5 boards)
        grid.draw(g);
        maze.drawMaze(g);

        // solution only shown when toggled by the SOLVE button
        if (showSolution)
            maze.drawSolution(g);
            
        tp.drawPortals(g, tp.curColor1, tp.curColor2);
        portalCounter++;
        
        //Draw circular teleportal animation
        if (portalCounter % 2 == 0)
        {
            int red = tp.curColor1.getRed() - 10;
            int green = tp.curColor1.getGreen() - 5;
            int blue = tp.curColor1.getBlue() - 12;
            if (red < 0 || green < 0 || blue < 0)
            {
                red = tp.lightColor.getRed();
                green = tp.lightColor.getGreen();
                blue = tp.lightColor.getBlue();
            }
            tp.curColor1 = new Color(red, green, blue);

            red = tp.curColor2.getRed() - 10;
            green = tp.curColor2.getGreen() - 5;
            blue = tp.curColor2.getBlue() - 12;
            if (red < 0 || green < 0 || blue < 0)
            {
                red = tp.lightColor.getRed();
                green = tp.lightColor.getGreen();
                blue = tp.lightColor.getBlue();
            }
            tp.curColor2 = new Color(red, green, blue);
        }    

        c.draw(g);
        // For zen mode, character not drawn
        if (!ch.hideChaser)
            ch.draw(g);

    }

    /**
     * Stores the character location and the maze type in a separate .ser 
     *      file using FileIO 
     *      
     * This method is currently not implemented (didn't have time) 
     * Would have been used in a save progress or load progress button
     */
    public void saveProgress() {
        try {
            // Uses serializable to store character obejct
            FileOutputStream charFile = new FileOutputStream(charFileName);
            ObjectOutputStream out = new ObjectOutputStream(charFile);

            // Writes character object in .ser file
            out.writeObject(c);

            out.close();
            charFile.close();

            System.out.println("Progress saved!");

            PrintWriter writer = new PrintWriter(mazeFileName);

            // Saves squareMoves variable, which stores the maze generation
            // saves first the [0][0][0], then [0][0][1]... [0][1][0].... [1][0][0]...

            for (int i = 0; i < maze.squareMoves.length; i++)
                for (int j = 0; j < maze.squareMoves[i].length; j++)
                    for (int k = 0; k < maze.squareMoves[i][j].length; k++)
                        writer.println(maze.squareMoves[i][j][k]);
            writer.close();

        } catch (IOException ex) {
            System.out.println("Caught IOException");
        }

    }

    /**
     * Gets the character location and the maze type from a separate .ser file 
     * 
     * This method is currently not implemented (didn't have time) 
     * Would have been used in a save progress or load progress button
     */
    public void loadProgress() {
        try {
            // Uses serializable to get Character object from .ser file
            FileInputStream charFile = new FileInputStream(charFileName);
            ObjectInputStream in = new ObjectInputStream(charFile);

            // Reads in character object
            c = (Character) in.readObject();

            System.out.println("Object deserialized");
            System.out.println("xBlock = " + c.xBlock);
            System.out.println("yBlock = " + c.yBlock);
            System.out.println("moves = " + c.moves);

            in.close();
            charFile.close();

            // Gets all the values in squareMoves file, stores in new squareMoves object
            FileReader reader = new FileReader(mazeFileName);
            Scanner scan = new Scanner(reader);
            int i = 0;
            int j = 0;
            int k = 0;

            while (scan.hasNext()) {
                maze.squareMoves[i][j][k] = scan.nextInt();
                k++;
                if (k == maze.squareMoves[i][j].length) {
                    k = 0;
                    j++;
                }
                if (j == maze.squareMoves[i].length) {
                    j = 0;
                    i++;
                }
                if (i == maze.squareMoves.length)
                    break;
            }

            reader.close();
            System.out.println("Progress saved!");

        } catch (IOException ex) {
            System.out.println("IOException is caught");
        }

        catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
    }
}