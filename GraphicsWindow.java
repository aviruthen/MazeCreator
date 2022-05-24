import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.*;

/**
 * Provides the interface for the Maze and the Game
 * 
 * Methods: 
 *          startTimer(): starts graphics timer 
 *          startMoveTimer(): starts character movement timer 
 *          actionPerformed(): handles any button presses or timer calls 
 *          keyPressed(): handles any keys pressed 
 *          getScore(): returns the score of the user when completing maze 
 *          saveScore(): saves scores to a .ser file using serializable 
 *          loadScores(): gets scores from a .ser file
 *          clearScores(): clears scores in the .ser file
 * 
 * @author Avi and Amar Ruthen
 * @version 12.21.2020
 */
public class GraphicsWindow extends JFrame implements ActionListener, KeyListener {
    int TIMER_DELAY; // milliseconds for updating graphics/motion,
                     // subject to change (see buttons)
    Timer t, moveTimer;
    String[] difficulties = { "Easy", "Medium", "Hard", "Zen" };
    String difficulty;

    // components and objects:
    JPanel displayPanel, scorePanel, topPanel, menuPanel, buttonPanel;
    JButton quitButton, solutionButton, mazeButton;
    JTextField info;
    JComboBox difficultySelector;
    JLabel scoreInfo;
    Display display;

    // size of window:
    int WIDTH;
    int HEIGHT;

    int numSquaresAcross; // number of blocks horizontally
    int numSquaresDown; // number of blocks vertically
    int squareSize; // size of each grid block (in pixels)

    // Extraneous (but needed) variables
    int moveTimerCount = 0;
    int MOVE_TIME = 3;
    int moveCode = -1;
    int chMoveCode = (int) (Math.random() * 4);
    int difCounter;

    // faceDirections (these values are always constant)
    int NORTH = 0;
    int EAST = 1;
    int SOUTH = 2;
    int WEST = 3;

    long startTime;
    long endTime;
    long gameTime;
    
    boolean inPortal1 = false;
    boolean inPortal2 = false;

    // Objects
    Grid grid;
    Maze maze;
    Character c;
    Chaser ch;
    TelePortal tp;
    ArrayList<HighScores> scores = new ArrayList<HighScores>();

    /**
     * Constructor for Window
     */
    public GraphicsWindow() {
        // calls constructor of the "super class"- JFrame - arg is titlebar name
        super("MazeCreator");

        // the JFrame/Graphics Window size:
        WIDTH = 810;
        HEIGHT = 610;

        TIMER_DELAY = 10; // SPEED of graphics, lower number = faster
        // (# of milliseconds between updates to graphics)

        difficulty = "Easy"; // default difficulty and size of maze
        difCounter = 0;
        numSquaresAcross = 12;
        numSquaresDown = 9;

        squareSize = WIDTH / numSquaresAcross;

        // Initialize objects
        grid = new Grid(0, 0, squareSize, numSquaresAcross, numSquaresDown);
        maze = new Maze(numSquaresAcross, numSquaresDown, squareSize);
        c = new Character(squareSize, numSquaresDown, numSquaresAcross);
        ch = new Chaser(squareSize, numSquaresDown, numSquaresAcross, difficulty);

        // Maze is generated and solved here!!!
        this.maze.generateMaze();
        this.maze.findSolution();
        tp = new TelePortal(squareSize, numSquaresAcross, numSquaresDown, maze);

        // Format graphics window
        display = new Display(WIDTH, HEIGHT, numSquaresAcross, grid, maze, c, ch, tp);
        topPanel = new JPanel();
        menuPanel = new JPanel();
        buttonPanel = new JPanel();
        scorePanel = new JPanel();
        displayPanel = new JPanel();

        displayPanel.setBackground(Color.WHITE);
        scorePanel.setBackground(new Color(128, 200, 216));
        buttonPanel.setBackground(new Color(125, 195, 100));

        // allows you to place things where you want them (see this.add() below)
        this.setLayout(new BorderLayout());

        // add the display to this JFrame:
        this.add(displayPanel, BorderLayout.CENTER);
        this.add(scorePanel, BorderLayout.EAST);
        this.add(topPanel, BorderLayout.NORTH);

        topPanel.add(buttonPanel, BorderLayout.EAST);
        topPanel.add(menuPanel, BorderLayout.WEST);

        displayPanel.add(display);

        // Create buttons

        quitButton = new JButton("QUIT");
        quitButton.addActionListener(this);
        quitButton.setActionCommand("quitButton");
        quitButton.setToolTipText("Click to exit the game");
        quitButton.setFocusable(false);

        solutionButton = new JButton("SOLVE");
        solutionButton.addActionListener(this);
        solutionButton.setActionCommand("solutionButton");
        solutionButton.setToolTipText("Reveal solution to the maze");
        solutionButton.setFocusable(false);

        mazeButton = new JButton("MAZE");
        mazeButton.addActionListener(this);
        mazeButton.setActionCommand("mazeButton");
        mazeButton.setToolTipText("Generate a new maze");
        mazeButton.setFocusable(false);

        startTime = System.nanoTime();

        scoreInfo = new JLabel("" + startTime);

        difficultySelector = new JComboBox(difficulties);
        difficultySelector.setSelectedIndex(0);
        difficultySelector.addActionListener(this);
        difficultySelector.setActionCommand("difficulty");
        difficultySelector.setFocusable(false);

        buttonPanel.add(quitButton);
        buttonPanel.add(solutionButton);
        buttonPanel.add(mazeButton);
        menuPanel.add(difficultySelector);
        scorePanel.add(scoreInfo);

        // final setup
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // Begin game (scores never loaded as we ran out of time)
        // loadScores();
        startTimer();
        startMoveTimer();
    }

    /**
     * Creates a Timer object and starts timer
     */
    public void startTimer() {
        t = new Timer(TIMER_DELAY, this);
        t.setActionCommand("timerFired");
        t.start();
    }

    /**
     * Timer for smoother graphics for Character movement
     */
    public void startMoveTimer() {
        moveTimer = new Timer(MOVE_TIME, this);
        moveTimer.setActionCommand("moveTimer");
        moveTimer.start();
    }

    /**
     * Called automatically when a button is pressed
     * 
     * @param e contains information about button that was pressed/sent
     *          automatically
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("timerFired")) // timer has fired
        {
            // Checks if Character is in the top right corner (winning position)
            // Otherwise it displays your moves and the time elapsed
            if (c.getGridX() == numSquaresAcross - 1 && c.getGridY() == numSquaresDown - 1)
                scoreInfo.setText("Your score is " + getScore());
            else {
                gameTime = (System.nanoTime() - startTime) / 1000000000;
                scoreInfo.setText("" + gameTime + " " + c.moves);
            }
            
            inPortal1 = Math.abs(c.xBlock + squareSize/4 - tp.xBlock1 - squareSize/2) <= squareSize/2 && 
                        Math.abs(c.yBlock + squareSize/4 - tp.yBlock1 - squareSize/2) <= squareSize/2;
            inPortal2 = Math.abs(c.xBlock + squareSize/4 - tp.xBlock2 - squareSize/2) <= squareSize/2 && 
                        Math.abs(c.yBlock + squareSize/4 - tp.yBlock2 - squareSize/2) <= squareSize/2;
                        
            repaint();            
        }
        // Called very often. Interactions are checked here!!
        if (e.getActionCommand().equals("moveTimer")) {
            // Move character
            if (c.canMove(maze.squareMoves, moveCode)) {
                difCounter = c.move(moveCode, difficulty, difCounter);
            }

            // If the chaser is in the middle of the square
            // Chaser will only change movements once it has fully travelled a block
            if ((((ch.xBlock - (squareSize / 4)) % squareSize) == 0)
                    && ((ch.yBlock - (squareSize / 4)) % squareSize == 0)) {
                ArrayList<Integer> directions = new ArrayList<Integer>();

                // Adds all possible movement directions in local ArrayList
                for (int d : maze.squareMoves[ch.getGridX()][ch.getGridY()])
                    if (d != -1)
                        directions.add(d);

                // If the chaser can only move in one direction, move that way
                if (directions.size() == 1) {
                    chMoveCode = directions.get(0);
                }
                // Remove option to move backwards, then randomly choose
                // From the remaining possible moves
                else {
                    for (int i = 0; i < directions.size(); i++)
                        if (directions.get(i) == ((chMoveCode + 2) % 4))
                            directions.remove(i);
                    chMoveCode = directions.get((int) (Math.random() * directions.size()));
                }
            }
            // Move the chaser
            difCounter = ch.move(chMoveCode, difficulty, difCounter);

            // If the chaser and character collide, reset the character at the bottom left
            if ((Math.abs(ch.xBlock - c.xBlock) < squareSize / 2 && Math.abs(ch.yBlock - c.yBlock) < squareSize / 2)
                    && !(c.getGridX() == numSquaresAcross - 1 && c.getGridY() == numSquaresDown - 1)) {
                c.xBlock = c.sizeAdj;
                c.yBlock = numSquaresDown * squareSize - (3 * c.sizeAdj);
            }
        }
        // Quits out of game of course....
        if (e.getActionCommand().equals("quitButton")) {
            System.exit(0);
        }
        // Shows a red path that displays the solution
        if (e.getActionCommand().equals("solutionButton")) {
            display.showSolution = !display.showSolution;
        }
        // Creates a new maze
        if (e.getActionCommand().equals("mazeButton")) {
            resetMaze();
        }
        // Constantly checks difficulty so that maze maintains correct size
        if (e.getActionCommand().equals("difficulty")) {
            difficulty = difficulties[difficultySelector.getSelectedIndex()];
            if (difficulty.equals("Easy")) {
                numSquaresAcross = 12;
                numSquaresDown = 9;
            } else if (difficulty.equals("Medium")) {
                numSquaresAcross = 20;
                numSquaresDown = 15;
            } else if (difficulty.equals("Hard")) {
                numSquaresAcross = 28;
                numSquaresDown = 21;
            }
            // zen difficulty, no chasers
            else {
                numSquaresAcross = 20;
                numSquaresDown = 15;
            }
            resetMaze();
        }
    }

    /**
     * Creates a new maze and resets scores 
     * 
     * Normally would save prior score to a HighScores object as well
     *      But we had to comment that part out due to time constrains
     */
    public void resetMaze() {
        // if (c.getGridX() == numSquaresAcross - 1 && 
        //    c.getGridY() == numSquaresDown - 1) {
        //         saveScore("Name", maze.squareMoves, c.moves);
        //         loadScores();
        // }

        // Reestablish all objects
        startTime = System.nanoTime();
        squareSize = WIDTH / numSquaresAcross;
        this.grid = new Grid(0, 0, squareSize, numSquaresAcross, numSquaresDown);
        this.maze = new Maze(numSquaresAcross, numSquaresDown, squareSize);
        this.maze.generateMaze();
        this.maze.findSolution();
        this.c = new Character(squareSize, numSquaresDown, numSquaresAcross);
        this.ch = new Chaser(squareSize, numSquaresDown, numSquaresAcross, difficulty);
        this.tp = new TelePortal(squareSize, numSquaresAcross, numSquaresDown, maze);

        display.c = c;
        display.grid = grid;
        display.ch = ch;
        display.maze = maze;
        display.tp = tp;
        // Determine whether chaser should be shown
        if (difficulty.equals("Zen"))
            ch.hideChaser = true;
        else
            ch.hideChaser = false;
    }

    /**
     * Called by the timer. Allows user to move
     * 
     * @param e The KeyEvent object to relay information about which key was pressed
     */
    public void keyPressed(KeyEvent e) {
        // Listens for a key press, tracks which key was pressed
        int code = e.getKeyCode();
        c.inMotion = true;

        // Move upward (until hitting wall or manually stopped)
        if (code == KeyEvent.VK_UP) {
            if (c.canMove(maze.squareMoves, NORTH)) {
                moveCode = NORTH;
                moveTimerCount = 0;
                difCounter = 0;
            }
        }
        // Move downward (until hitting wall or manually stopped)
        if (code == KeyEvent.VK_DOWN) {
            if (c.canMove(maze.squareMoves, SOUTH)) {
                moveCode = SOUTH;
                moveTimerCount = 0;
                difCounter = 0;
            }
        }
        // Move left (until hitting wall or manually stopped)
        if (code == KeyEvent.VK_LEFT) {
            if (c.canMove(maze.squareMoves, WEST)) {
                moveCode = WEST;
                moveTimerCount = 0;
                difCounter = 0;
            }
        }
        // Move right (until hitting wall or manually stopped)
        if (code == KeyEvent.VK_RIGHT) {
            if (c.canMove(maze.squareMoves, EAST)) {
                moveCode = EAST;
                moveTimerCount = 0;
                difCounter = 0;
            }
        }
        // Manually stop motion (if needed) and entering portals
        if (code == KeyEvent.VK_SPACE)
        {
            if (inPortal1)
            {
                c.xBlock = tp.xBlock2 + squareSize/4;
                c.yBlock = tp.yBlock2 + squareSize/4;
            }
            else if (inPortal2)
            {
                c.xBlock = tp.xBlock1 + squareSize/4;
                c.yBlock = tp.yBlock1 + squareSize/4;
            }
            c.inMotion = false;
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    /**
     * Calculates the user score (based on the number of moves and the time elapsed)
     * 
     * @return the score
     */
    public long getScore() {
        double length = (double) (maze.solutionSequence.length);
        return (long) (1000.0 * ((length / c.moves) * (1 - gameTime / (length * 5))));
    }

    /**
     * Saves HighScores objects to the .ser file 
     * Method never implemented due to time constraints
     * 
     * @param name        the name of the user
     * @param squareMoves the maze used (a triple array that stores all maze gen
     *                    info)
     * @param moves       the number of moves taken to complete the maze
     */
    public void saveScore(String name, int[][][] squareMoves, int moves) {

        scores.add(new HighScores(name, (int) getScore(), Time.getTime(), moves, squareMoves));

        // Obtain scores from .ser file using serializable, then sort into increasing
        // order
        try {
            // clearScores();
            FileOutputStream scoresFile = new FileOutputStream("HighScores.ser");
            ObjectOutputStream out = new ObjectOutputStream(scoresFile);

            // Put the ArrayList<Integer> into an int[] to access .sort() method
            int[] scoreVals = new int[scores.size()];
            for (int i = 0; i < scores.size(); i++)
                scoreVals[i] = scores.get(i).score;
            Arrays.sort(scoreVals);

            ArrayList<HighScores> scoreTemp = new ArrayList<HighScores>();

            // Goes in reverse order
            // If HighScores object has same score, adds it to ArrayList
            for (int i = scoreVals.length - 1; i >= 0; i--)
                for (HighScores h : scores)
                    if (h.score == scoreVals[i])
                        scoreTemp.add(h);

            scores.clear();

            for (HighScores h : scoreTemp)
                scores.add(h);

            // Rewrites new score list into HighScores file
            out.writeObject(scores);
            out.close();
            scoresFile.close();
        } catch (IOException ex) {
            System.out.println("Caught IOException");
        }
    }

    /**
     * Obtains scores from .ser file 
     * Method never implemented due to time constraints
     */
    public void loadScores() {

        // Get scores from the .ser file using serializable
        try {
            FileInputStream scoresFile = new FileInputStream("HighScores.ser");
            ObjectInputStream in = new ObjectInputStream(scoresFile);

            scores.clear();
            scores = (ArrayList<HighScores>) in.readObject();

            in.close();
            scoresFile.close();

        } catch (IOException ex) {
        }

        catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
    }

    /**
     * Deletes all scores in the HighScores file (if needed) 
     * Method never implemented due to time constraints
     */
    public void clearScores() {
        try {
            FileOutputStream scoresFile = new FileOutputStream("HighScores.ser");
            scoresFile.close();
        } catch (IOException e) {
        }
    }
}