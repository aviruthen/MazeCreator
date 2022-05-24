/**
 * All moving objects in the Maze inherit from this super class
 * 
 * Methods:
 *           move(): changes the position of the object in given direction
 *           getGridX(): returns the horizontal block position
 *           getGridX(x): returns horizontal block position for given x-coord
 *           getGridY(): returns vertical block position
 *           getGridY(y); returns vertical block position for given y-coord
 *           blockTraversed: checks if object has changed block position
 *           canMove(): checks if character will ram into wall or can move
 *                      in a given direction
 *           
 *
 * @author Amar and Avi Ruthen
 * @version May 22, 2020
 */
public class AnimateObject {
    int xBlock;
    int yBlock;
    int squareSize;
    int sizeAdj;
    int moves;
    int gridHeight, gridWidth;
    boolean inMotion = false;

    // instance variables - replace the example below with your own
    public AnimateObject(int squareSize, int gridHeight, int gridWidth) {
        sizeAdj = squareSize / 4;
        xBlock = sizeAdj;
        yBlock = 600 - (3 * sizeAdj);
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
        this.squareSize = squareSize;
    }

    /*public void move(int direction) {
        int formerX = xBlock;
        int formerY = yBlock;
        switch (direction) {
            case 0:
                yBlock--;
                break;
            case 1:
                xBlock++;
                break;
            case 2:
                yBlock++;
                break;
            case 3:
                xBlock--;
                break;
            default:
                break;
        }
        if (blockTraversed(formerX, formerY))
            moves++;
    }*/

    /*public boolean canMove(int[][][] squareMoves, int direction) {
        //if ((yBlock + (3 * sizeAdj)) % squareSize != 0 || (xBlock - sizeAdj) % squareSize != 0)
            //return true;

        for (int i = 0; i < squareMoves[getGridX()][getGridY()].length; i++) {
            if (squareMoves[getGridX()][getGridY()][i] == direction)
                return true;
        }
        return false;
    }*/
    
    /**
     * Returns the column the moving object is occupying
     * 
     * @return the column value
     */
    public int getGridX() {
        return (xBlock + squareSize / 4) / squareSize;
    }
    
    /**
     * Returns the column for the given x coordinate
     * 
     * @param x : the x coordinate to test
     * 
     * @return the column value
     */
    public int getGridX(int x) {
        return (x + squareSize / 4) / squareSize;
    }

    /**
     * Returns the row the moving object is occupying
     * 
     * @return the row value
     */
    public int getGridY() {
        return gridHeight - 1 - (yBlock + squareSize / 4) / squareSize;
    }
    
    /**
     * Returns the row for the given y coordinate
     * 
     * @return the row value
     */
    public int getGridY(int y) {
        return gridHeight - 1 - (y + squareSize / 4) / squareSize;
    }
    
    /**
     * Determines if object has fully traversed a block
     * 
     * @return true if the block the character now occupies 
     *         Is different from before
     */
    public boolean blockTraversed(int formerX, int formerY) {
        if (getGridX() == getGridX(formerX) && getGridY() == getGridY(formerY))
            return false;
        return true;
    }
    
    /**
     * Enables object to change position
     * 
     * @param direction the direction to travel
     * @param difficulty (needed for the size of the block)
     * @param difCounter (needed to set the speed of the object)
     * 
     * @return difCounter the integer that controls which ticks to move
     */
    public int move(int direction, String difficulty, int difCounter) {
        int formerX = xBlock;
        int formerY = yBlock;
        
        if (difCounter == 0 || difCounter == 1)
        {
            switch (direction) {
                case 0:
                    yBlock--;
                    break;
                case 1:
                    xBlock++;
                    break;
                case 2:
                    yBlock++;
                    break;
                case 3:
                    xBlock--;
                    break;
                default:
                    break;
            }
            if (blockTraversed(formerX, formerY))
                moves++;
        }
        
        //Based on the difficulty, the object will either move
        //Every timer call or some fraction less
        switch (difficulty)
        {
            case "Easy":
                difCounter = 0;
                break;
            case "Medium":
                difCounter += 2;
                difCounter %= 2;
                break;
            case "Hard":
                difCounter += 2;
                difCounter %= 3;
            default:
                break;
        }
        return difCounter;
    }

    /**
     * Determines if a moving object can move in the given direction
     * @param squareMoves All possible move values at a given square
     * @param direction Intended travel location
     * @return
     */
    public boolean canMove(int[][][] squareMoves, int direction) {
        //object cannot move
        if (!inMotion)
            return false;
        //central location of the object
        int charCenterX = (xBlock + squareSize/4) % squareSize;
        int charCenterY = (yBlock + squareSize/4) % squareSize;

        boolean canMove = false;

        //movement direction
        switch (direction)
        {
            //no intended movement
            case -1:
                return false;
            //NORTH
            case 0:
                //object is centered on the bottom portion of the square
                if (charCenterY > squareSize/4)
                {
                    //edge of the circle within the square
                    if (charCenterX >= squareSize/4 && charCenterX <= 3*squareSize/4)
                        return true;
                    canMove = true;
                }
                //edge of the object passes through top of the square
                if (charCenterY <= squareSize/4)
                {
                    //checks if object can move up in that direction
                    for (int i = 0; i < squareMoves[0][0].length; i++)
                    {
                        if (squareMoves[getGridX()][getGridY()][i] == 0)
                        {
                            return true;
                        }
                    }
                }
                //left edge of the circle is beyond square boundary
                if (charCenterX < squareSize/4)
                {
                    //opening on the left side
                    for (int i = 0; i < squareMoves[0][0].length; i++)
                    {
                        if (squareMoves[getGridX()][getGridY()][i] == 3 && canMove)
                            return true;
                    }
                }
                //right edge of the circle is beyond square boundary
                if (charCenterX > 3*squareSize/4)
                {
                    //opening on the right side
                    for (int i = 0; i < squareMoves[0][0].length; i++)
                    {
                        if (squareMoves[getGridX()][getGridY()][i] == 1 && canMove)
                            return true;
                    }
                }
                break;
            //EAST
            case 1:
                //object is centered on the left portion of the square
                if (charCenterX < 3*squareSize/4)
                {
                    //edge of the circle within the square
                    if (charCenterY >= squareSize/4 && charCenterY <= 3*squareSize/4)
                        return true;
                    canMove = true;
                }
                //edge of the object passes through right of the square
                if (charCenterX >= 3*squareSize/4)
                {
                    //checks if object can move right in that direction
                    for (int i = 0; i < squareMoves[0][0].length; i++)
                    {
                        if (squareMoves[getGridX()][getGridY()][i] == 1)
                        {
                            return true;
                        }
                    }
                }
                //upper edge of circle is beyond boundary
                if (charCenterY < squareSize/4)
                {
                    for (int i = 0; i < squareMoves[0][0].length; i++)
                    {
                        if (squareMoves[getGridX()][getGridY()][i] == 0 && canMove)
                            return true;
                    }
                }
                //lower edge of circle is beyond boundary
                if (charCenterY > 3*squareSize/4)
                {
                    for (int i = 0; i < squareMoves[0][0].length; i++)
                    {
                        if (squareMoves[getGridX()][getGridY()][i] == 2 && canMove)
                            return true;
                    }
                }
                break;
            //SOUTH (similar process as NORTH)
            case 2:
                if (charCenterY < 3*squareSize/4)
                {
                    if (charCenterX >= squareSize/4 && charCenterX <= 3*squareSize/4)
                        return true;
                    canMove = true;
                }
                if (charCenterY >= 3*squareSize/4)
                {
                    for (int i = 0; i < squareMoves[0][0].length; i++)
                    {
                        if (squareMoves[getGridX()][getGridY()][i] == 2)
                        {
                            return true;
                        }
                    }
                }
                if (charCenterX < squareSize/4)
                {
                    for (int i = 0; i < squareMoves[0][0].length; i++)
                    {
                        if (squareMoves[getGridX()][getGridY()][i] == 3 && canMove)
                            return true;
                    }
                }
                if (charCenterX > 3*squareSize/4)
                {
                    for (int i = 0; i < squareMoves[0][0].length; i++)
                    {
                        if (squareMoves[getGridX()][getGridY()][i] == 1 && canMove)
                            return true;
                    }
                }
                break;
            //WEST (similar process as EAST)
            case 3:
                if (charCenterX > squareSize/4)
                {
                    if (charCenterY >= squareSize/4 && charCenterY <= 3*squareSize/4)
                        return true;
                    canMove = true;
                }
                if (charCenterX <= squareSize/4)
                {
                    for (int i = 0; i < squareMoves[0][0].length; i++)
                    {
                        if (squareMoves[getGridX()][getGridY()][i] == 3)
                        {
                            return true;
                        }
                    }
                }
                if (charCenterY < squareSize/4)
                {
                    for (int i = 0; i < squareMoves[0][0].length; i++)
                    {
                        if (squareMoves[getGridX()][getGridY()][i] == 0 && canMove)
                            return true;
                    }
                }
                if (charCenterY > 3*squareSize/4)
                {
                    for (int i = 0; i < squareMoves[0][0].length; i++)
                    {
                        if (squareMoves[getGridX()][getGridY()][i] == 2 && canMove)
                            return true;
                    }
                }
                break;
            default:
                break;
        }

        return false; //default is cannot move
    }
}