import java.util.ArrayList;
import java.awt.*;

/**
 * All the code to generate a grid-based maze and solve the maze based
 * on its generation.
 * 
 * Useful Methods:
 *      generateMaze(): Generates a random maze (see method for details)
 *      findSolution(): Solves the maze created in generateMaze()
 *      drawMaze(g): Draws the maze onto the grid
 *      drawSolution(g): Draws the solution onto the maze
 */
public class Maze {
    
    ArrayList<Integer> moves; //tracks the raw moves when the maze is generated
    int[][][] squareMoves; //provides all possible moves at any square in maze
    int[] solutionSequence; //the sequence of moves that will solve the maze
    int gridWidth, gridHeight; //the dimensions of the grid
    int squareSize; //the size of the width of a square

    // faceDirections (these values are always constant)
    int NORTH = 0;
    int EAST = 1;
    int SOUTH = 2;
    int WEST = 3;

    Utility u;

    /**
     * Constructor for Maze Class
     * @param gridWidth The width of the maze
     * @param gridHeight The height of the maze
     * @param squareSize The size of the width of a square
     */
    public Maze(int gridWidth, int gridHeight, int squareSize) {
        //stores values of arguments into instance variables
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.squareSize = squareSize;

        moves = new ArrayList<Integer>();

        /*
         * Fills all values of squareMoves with -1 (cannot move)
         * Function of squareMoves: Each array at squareMoves[x][y] contains up
         * to four possible directions the character can move in:
         *   squareMoves[x][y][0]: The primary move when the maze is generated 
         *      (the direction the character likely is headed in when it reaches
         *      that square)
         *   squareMoves[x][y][1]: The opposite move to the primary move
         *      (Ex: If squareMoves[x][y][0] is NORTH, squareMoves[x][y][1] is SOUTH)
         *   squareMoves[x][y][2]: The first alternative direction (not -1 if 
         *      the character can head in multiple directions [fork block])
         *   squareMoves[x][y][3]: The second alternative direction (rare)
         */
        squareMoves = new int[gridWidth][gridHeight][4];
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                for (int k = 0; k < squareMoves[0][0].length; k++)
                    squareMoves[i][j][k] = -1;
            }
        }
        u = new Utility();
    }

    /**
     * Generates a random maze with one unique solution (starting from the 
     * bottom left and ending at the top left)
     */
    public void generateMaze() {
        //keeps track of which squares have already been traversed
        boolean[][] squares = new boolean[gridWidth][gridHeight];
        int numMoves = 0;
        int prevDirection = -1;
        int travelDirection = -1;
        //coordinates are doubles to make it easier to draw
        double currentX = 0.5;
        double currentY = 0.5;
        //character starts at first square
        squares[u.floor(currentX)][u.floor(currentY)] = true;

        boolean successfulMove = false;
       
        //checks if program can no longer traverse an empty adjacent square
        boolean trapped = false;
        boolean completed = false;

        /**
         * The program will randomly traverse the grid. However, the program
         * cannot traverse blocks it has already visited. If the program is 
         * trapped between blocks it cannot move to, the program will find the 
         * first block with an opening as the new starting point (based either
         * on columns or rows)
         */
        while (!completed) {
            if (successfulMove)
                prevDirection = travelDirection;

            travelDirection = u.randomInteger(3); // random travel direction
            // cannot travel backwards, backwards becomes forward
            if ((travelDirection + 2) % 4 == prevDirection)
                travelDirection = prevDirection;

            double formerX = currentX;
            double formerY = currentY;

            /*
             * Assume move occurs
             */
            // left-right direction
            if (travelDirection % 2 == 1)
                currentX += (double) (-1 * (travelDirection - 2));
            // north-south direction
            else
                currentY += (double) (-1 * (travelDirection - 1));

            //determines if the program can successfully travel in direction
            successfulMove = isValidMove(currentX, currentY, squares);

            //move can occur
            if (successfulMove) {
                //current location has now been raversed
                squares[u.floor(currentX)][u.floor(currentY)] = true;
                //adds travelDirection as a primary direction
                if (!trapped)
                    squareMoves[u.floor(formerX)][u.floor(formerY)][0] = travelDirection;
                //adds travelDirection as an alternative direction
                else if (trapped) {
                    if (squareMoves[u.floor(formerX)][u.floor(formerY)][2] == -1)
                        squareMoves[u.floor(formerX)][u.floor(formerY)][2] = travelDirection;
                    else
                        squareMoves[u.floor(formerX)][u.floor(formerY)][3] = travelDirection;
                }
                //adds the opposite direction to the current travelDirection
                squareMoves[u.floor(currentX)][u.floor(currentY)][1] = (travelDirection + 2) % 4;
                moves.add(travelDirection);
                numMoves++;
            } 
            //resets location
            else {
                currentX = formerX;
                currentY = formerY;
            }

            //determines if the program is now trapped as a result of the move
            if (successfulMove) {
                trapped = false;
                if (isTrapped(u.floor(currentX), u.floor(currentY), squares))
                    trapped = true;
            }

            // move to another square location that is not trapped
            if (trapped) {
                //chooses if the new location is assessed based on row or by column
                int chooseAnalyze = u.randomInteger(1);
                //by column
                if (chooseAnalyze == 0) {
                    for (int i = 0; i < gridWidth; i++) {
                        for (int j = 0; j < gridHeight; j++) {
                            // squares has already been traversed but is not trapped
                            if (squares[i][j] && !isTrapped(i, j, squares)) {
                                //sets to new coordinates and resets other variables
                                currentX = (double) (i) + 0.5;
                                currentY = (double) (j) + 0.5;
                                i += gridWidth;
                                j += gridHeight;
                                prevDirection = -1;
                                travelDirection = -1;
                            }
                        }
                    }
                } 
                //by row
                else {
                    for (int i = 0; i < gridHeight; i++) {
                        for (int j = 0; j < gridWidth; j++) {
                            // squares has already been traversed but is not trapped
                            if (squares[j][i] && !isTrapped(j, i, squares)) {
                                //sets to new coordinates and resets other variables
                                currentX = (double) (j) + 0.5;
                                currentY = (double) (i) + 0.5;
                                j += gridWidth;
                                i += gridHeight;
                                prevDirection = -1;
                                travelDirection = -1;
                            }
                        }
                    }
                }
            }
            //program should traverse all squares (started at first one)
            if (numMoves == gridWidth * gridHeight - 1)
                completed = true;
        }
    }

    /**
     * Determines a solution based on squareMoves values
     */
    public void findSolution() {
        //maze has not been generated yet
        if (moves.size() == 0)
            return;
        //keeps track of which squares have already been traversed
        boolean[][] squares = new boolean[gridWidth][gridHeight];
        //character starts at first square
        squares[0][0] = true;
        //tracks the forkBlocks the program has traversed in a stack format
        ArrayList<ArrayList<Integer>> forkBlocks = new ArrayList<ArrayList<Integer>>();

        //tracks the current solution sequence
        ArrayList<Integer> solSequence = new ArrayList<Integer>();
        int currentX = 0;
        int currentY = 0;
        int movesAfterFork = 0; //number of moves since traversing a fork block

        boolean complete = false;

        /**
         * Alterations are made to squareMoves to allow for the solution, so
         * a local variable is used
         */
        int[][][] sMoves = new int[squareMoves.length][squareMoves[0].length][squareMoves[0][0].length];
        for (int i = 0; i < squareMoves.length; i++) {
            for (int j = 0; j < squareMoves[0].length; j++) {
                for (int k = 0; k < squareMoves[0][0].length; k++) {
                    sMoves[i][j][k] = squareMoves[i][j][k];
                }
            }
        }

        //continue looping until program reaches the end square
        while (!complete) {
            //swithc the primary direction
            switch (sMoves[currentX][currentY][0]) {
                //no primary direction (trapped square)
                case -1:
                    //remove all sequence values after fork block
                    for (int i = 0; i < movesAfterFork + 1; i++)
                        solSequence.remove(solSequence.size() - 1);
                    movesAfterFork = 0;
                    //change location to fork block location
                    currentX = forkBlocks.get(forkBlocks.size() - 1).get(0);
                    currentY = forkBlocks.get(forkBlocks.size() - 1).get(1);
                    /*
                     * Program assesses if there are any alternative directions
                     * available to move in. If so, the program moves in that
                     * direction and replaces sMoves with -1 (so it can no longer
                     * move that way). If not, the program goes to the next fork
                     * block and repeats the same process.
                     */
                    while (sMoves[currentX][currentY][2] == -1 && sMoves[currentX][currentY][3] == -1) {
                        for (int j = 0; j < forkBlocks.get(forkBlocks.size() - 1).get(2) + 1; j++)
                            solSequence.remove(solSequence.size() - 1);
                        forkBlocks.remove(forkBlocks.size() - 1);
                        currentX = forkBlocks.get(forkBlocks.size() - 1).get(0);
                        currentY = forkBlocks.get(forkBlocks.size() - 1).get(1);
                    }
                    //fork block has an alternative direction
                    if (sMoves[currentX][currentY][2] != -1) {
                        int formerX = currentX;
                        int formerY = currentY;
                        //program moves in alt direction, adds to sequence
                        switch (sMoves[currentX][currentY][2]) {
                            case 0:
                                currentY++;
                                solSequence.add(0);
                                break;
                            case 1:
                                currentX++;
                                solSequence.add(1);
                                break;
                            case 2:
                                currentY--;
                                solSequence.add(2);
                                break;
                            case 3:
                                currentX--;
                                solSequence.add(3);
                                break;
                            default:
                                break;
                        }
                        sMoves[formerX][formerY][2] = -1; //no longer has alt
                    } 
                    //fork block has second alternative direction
                    else {
                        int formerX = currentX;
                        int formerY = currentY;
                        switch (sMoves[currentX][currentY][3]) {
                            case 0:
                                currentY++;
                                solSequence.add(0);
                                break;
                            case 1:
                                currentX++;
                                solSequence.add(1);
                                break;
                            case 2:
                                currentY--;
                                solSequence.add(2);
                                break;
                            case 3:
                                currentX--;
                                solSequence.add(3);
                                break;
                            default:
                                break;
                        }
                        sMoves[formerX][formerY][3] = -1; //no longer has alt
                    }
                    break;
                //NORTH
                case 0:
                    //current block is a fork block
                    if (sMoves[currentX][currentY][2] != -1) {
                        //adds a new fork block to the arraylist
                        forkBlocks.add(new ArrayList<Integer>());
                        forkBlocks.get(forkBlocks.size() - 1).add(currentX);
                        forkBlocks.get(forkBlocks.size() - 1).add(currentY);
                        //stores the current coordinates and moves after fork
                        //in the fork block array
                        forkBlocks.get(forkBlocks.size() - 1).add(movesAfterFork);
                        movesAfterFork = 0;
                    } 
                    //current block is not a fork block
                    else
                        movesAfterFork++;
                    currentY++;
                    solSequence.add(0);
                    break;
                //EAST (same process as NORTH)
                case 1:
                    if (sMoves[currentX][currentY][2] != -1) {
                        forkBlocks.add(new ArrayList<Integer>());
                        forkBlocks.get(forkBlocks.size() - 1).add(currentX);
                        forkBlocks.get(forkBlocks.size() - 1).add(currentY);
                        forkBlocks.get(forkBlocks.size() - 1).add(movesAfterFork);
                        movesAfterFork = 0;
                    } else
                        movesAfterFork++;
                    currentX++;
                    solSequence.add(1);
                    break;
                //SOUTH (same process as NORTH)
                case 2:
                    if (sMoves[currentX][currentY][2] != -1) {
                        forkBlocks.add(new ArrayList<Integer>());
                        forkBlocks.get(forkBlocks.size() - 1).add(currentX);
                        forkBlocks.get(forkBlocks.size() - 1).add(currentY);
                        forkBlocks.get(forkBlocks.size() - 1).add(movesAfterFork);
                        movesAfterFork = 0;
                    } else
                        movesAfterFork++;

                    currentY--;
                    solSequence.add(2);
                    break;
                //WEST (same process as NORTH)
                case 3:
                    if (sMoves[currentX][currentY][2] != -1) {
                        forkBlocks.add(new ArrayList<Integer>());
                        forkBlocks.get(forkBlocks.size() - 1).add(currentX);
                        forkBlocks.get(forkBlocks.size() - 1).add(currentY);
                        forkBlocks.get(forkBlocks.size() - 1).add(movesAfterFork);
                        movesAfterFork = 0;
                    } else
                        movesAfterFork++;

                    currentX--;
                    solSequence.add(3);
                    break;
                default:
                    break;
            }
            squares[currentX][currentY] = true; //block has been traversed
            //program has reached the end block
            if (squares[gridWidth - 1][gridHeight - 1])
                complete = true;
        }
        //converts solution sequence to an array and stores in instance variable
        solutionSequence = new int[solSequence.size()];
        for (int i = 0; i < solutionSequence.length; i++)
            solutionSequence[i] = solSequence.get(i);
    }

    /**
     * Moves program based on current location and travelDirection
     * @param currentX The x-coordinate of the program (left is 0)
     * @param currentY The y-coordinate of the program (bottom is 0)
     * @param travelDirection The direction the program is heading in
     * @return integer array with the x coordinate, then the y-coordinate
     */
    public int[] moveInDirection(int currentX, int currentY, int travelDirection) {
        switch (travelDirection) {
            //NORTH
            case 0:
                currentY++;
                break;
            //EAST
            case 1:
                currentX++;
                break;
            //SOUTH
            case 2:
                currentY--;
                break;
            //WEST
            case 3:
                currentX--;
                break;
            default:
                break;
        }
        return new int[] { currentX, currentY };
    }

    /**
     * Determines if the program is surrounded by blocks it has already traversed
     * @param currentX The x-coordinate of the program (left is 0)
     * @param currentY The y-coordinate of the program (bottom is 0)
     * @param squares Tracks which squares have already been traversed
     * @return true if the program is surrounded by traversed blocks
     */
    public boolean isTrapped(int currentX, int currentY, boolean[][] squares) {
        /*
         * Checks four adjacent blocks (above, below, left, right) and determines
         * if they have already been traveled on
         * Also checks if the four adjacent blocks are within the grid
         */
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (Math.abs(i) + Math.abs(j) != 2 && currentX + i >= 0 && currentX + i < gridWidth && currentY + j >= 0
                        && currentY + j < gridHeight && !squares[currentX + i][currentY + j])
                    return false;
            }
        }
        return true;
    }

    /*
    public String openSquare(int currentX, int currentY, boolean[][] squares) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (Math.abs(i) + Math.abs(j) != 2 && currentX + i >= 0 && currentX + i < gridWidth && currentY + j >= 0
                        && currentY + j < gridHeight && squares[currentX + i][currentY + j]) {
                    if (i == 1)
                        return "right";
                    if (i == -1)
                        return "left";
                    if (j == 1)
                        return "top";
                    if (j == -1)
                        return "bottom";
                }
            }
        }
        return "none";
    }
    */

    /**
     * Determines if program is in a valid location (thus resulting in a valid move)
     * @param currentX The x-coordinate of the program (left is 0)
     * @param currentY The y-coordinate of the program (bottom is 0)
     * @param squares Tracks which squares have already been traversed
     * @return true if the location is valid
     */
    public boolean isValidMove(double currentX, double currentY, boolean[][] squares) {
        // move goes off the board horizontally
        if (u.floor(currentX) < 0 || u.floor(currentX) >= gridWidth)
            return false;
        // move goes off the board vertically
        if (u.floor(currentY) < 0 || u.floor(currentY) >= gridHeight)
            return false;
        // square has already been traversed
        if (squares[u.floor(currentX)][u.floor(currentY)])
            return false;
        return true;
    }

    /**
     * Draws a horizontal line from x1 to x2 at the given y level
     * @param g Graphics object with methods to draw lines
     * @param x1 Starting x-coordinate
     * @param x2 Ending x-coordinate
     * @param y The row where the line will be drawn
     */
    public void drawHLine(Graphics g, int x1, int x2, int y) {
        int level = (gridHeight - y) * squareSize;
        g.drawLine(x1 * squareSize, level, x2 * squareSize, level);
    }

    /**
     * Draws a vertical line from y1 to y2 at the given x level
     * @param g Graphics object with methods to draw lines
     * @param y1 Starting y-coordinate
     * @param y2 Ending y-coordinate
     * @param x The column where the line will be drawn
     */
    public void drawVLine(Graphics g, int y1, int y2, int x) {
        int level = x * squareSize;
        g.drawLine(level, (gridHeight - y1) * squareSize, level, (gridHeight - y2) * squareSize);
    }

    /**
     * Draws a maze line based on specified criteria
     * @param g Graphics object with methods to draw lines
     * @param cX Current x-coordinate of program
     * @param cY Current y-coordinate of program
     * @param location The location to draw the line relative to the square
     */
    public void drawMazeLine(Graphics g, double cX, double cY, String location) {
        if (location.equals("left"))
            drawVLine(g, u.floor(cY), u.ceil(cY), u.floor(cX));
        else if (location.equals("right"))
            drawVLine(g, u.floor(cY), u.ceil(cY), u.ceil(cX));
        else if (location.equals("top"))
            drawHLine(g, u.floor(cX), u.ceil(cX), u.ceil(cY));
        else if (location.equals("bottom"))
            drawHLine(g, u.floor(cX), u.ceil(cX), u.floor(cY));
    }

    /**
     * Draws maze based on generation through squareMoves
     * @param g Graphics object with methods to draw lines
     */
    public void drawMaze(Graphics g) {
        //maze has not been generated yet
        if (squareMoves[0][0][0] == -1)
            return;
        /*
         * Program draws a white line over the grid to indicate an opening
         */
        g.setColor(Color.WHITE);
        for (int i = 0; i < squareMoves.length; i++) {
            for (int j = 0; j < squareMoves[0].length; j++) {
                for (int k = 0; k < squareMoves[0][0].length; k++) {
                    switch (squareMoves[i][j][k]) {
                        //no move
                        case -1:
                            break;
                        //NORTH
                        case 0:
                            drawMazeLine(g, i + 0.5, j + 0.5, "top");
                            break;
                        //EAST
                        case 1:
                            drawMazeLine(g, i + 0.5, j + 0.5, "right");
                            break;
                        //SOUTH
                        case 2:
                            drawMazeLine(g, i + 0.5, j + 0.5, "bottom");
                            break;
                        //WEST
                        case 3:
                            drawMazeLine(g, i + 0.5, j + 0.5, "left");
                            break;
                        default:
                            break;
                    }
                }

            }
        }
    }

    /**
     * Draws the solution to the maze based on the solution sequence
     * @param g Graphics object with methods to draw lines
     */
    public void drawSolution(Graphics g) {
        //solution has not been found yet
        if (solutionSequence == null)
            return;
        g.setColor(Color.RED);
        //tracks location as 0.5 so that the line is centered on the square
        double currentX = 0.5;
        double currentY = 0.5;
        double formerX = 0.5;
        double formerY = 0.5;
        //changes location based on move in the solution sequence
        for (int i = 0; i < solutionSequence.length; i++) {
            formerX = currentX;
            formerY = currentY;
            switch (solutionSequence[i]) {
                case 0:
                    currentY += 1.0;
                    break;
                case 1:
                    currentX += 1.0;
                    break;
                case 2:
                    currentY -= 1.0;
                    break;
                case 3:
                    currentX -= 1.0;
                    break;
                default:
                    break;
            }
            //draws line from previous location to new location
            g.drawLine((int) (formerX * squareSize), (int) ((gridHeight - formerY) * squareSize),
                    (int) (currentX * squareSize), (int) ((gridHeight - currentY) * squareSize));
        }
    }
}