import java.awt.*;
public class TelePortal
{
    /**
     * The pixel locations of the teleportals in the window
     */
    int xBlock1;
    int yBlock1;
    int xBlock2;
    int yBlock2;

    /**
     * The grid locations of the two teleportals
     */
    int[] firstCoords;
    int[] secondCoords;

    int gridWidth, gridHeight; //the dimensions of the grid
    int squareSize; //the size of the width of a square

    Color lightColor; //the lightest color of the teleportal

    //the current colors of the two teleportals
    Color curColor1;
    Color curColor2;

    /**
     * Constructor for the Teleportal Class
     * @param gridWidth The width of the maze
     * @param gridHeight The height of the maze
     * @param squareSize The size of the width of a square
     * @param m Themaze that the teleportals will be generated on
     */
    public TelePortal(int squareSize, int gridWidth, int gridHeight, Maze m)
    {
        //stores values of arguments into instance variables
        this.squareSize = squareSize;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        //sets lightest color and sets curColor2 to be halfway between 
        //light and dark
        lightColor = new Color(200, 100, 250);
        curColor1 = lightColor;
        curColor2 = new Color(100, 50, 130);
        
        firstCoords = new int[2];
        secondCoords = new int[2];
        
        //determines random coords and stores them into pixel locations
        int[] testCoords = getRandomCoords();
        xBlock1 = testCoords[0];
        yBlock1 = testCoords[1];
        xBlock2 = testCoords[2];
        yBlock2 = testCoords[3];

        //converts pixel locations into grid coordinates
        firstCoords[0] = getFirstGridX();
        firstCoords[1] = getFirstGridY();
        secondCoords[0] = getSecondGridX();
        secondCoords[1] = getSecondGridY();
        //while (distanceBetweenPortals(m) <= gridWidth*gridHeight/10);
    }
    /**
     * Creates random coordinates for the two teleportals. The teleportals
     * cannot have the same coordinates
     * @return The coordinates of the two teleportals
     */
    public int[] getRandomCoords()
    {
        //select a random x and y for the first teleportal
        int x1 = (int)(Math.random()*gridWidth);
        int y1 = (int)(Math.random()*gridHeight);

        int x2 = -1;
        int y2 = -1;
        //select a different random x for the second teleportal
        do
        {
            x2 = (int)(Math.random()*gridWidth);
        } while (x2 == x1);
        //select a different random y for the second teleportal
        do
        {
            y2 = (int)(Math.random()*gridHeight);
        } while (y2 == y1);
        //convert random grid coordinates to pixel coordinates
        x1 = x1*squareSize;
        y1 = (gridHeight - y1 - 1)*squareSize;
        x2 = x2*squareSize;
        y2 = (gridHeight - y2 - 1)*squareSize;
        return new int[]{x1, y1, x2, y2};
    }
    public void drawPortals(Graphics g, Color firstColor, Color secondColor)
    {
        //colors for the portals have to follow a specific purple scheme
        if (firstColor.getRed() % 4 != 0 && firstColor.getGreen() % 2 != 0 &&
        firstColor.getBlue() % 5 != 0 && firstColor.getRed() != 2*firstColor.getGreen()
        && firstColor.getBlue() * 2.5 != (double)(firstColor.getBlue()))
            return;
        //colors for the portals have to follow a specific purple scheme
        if (secondColor.getRed() % 4 != 0 && secondColor.getGreen() % 2 != 0 &&
        secondColor.getBlue() % 5 != 0 && secondColor.getRed() != 2*secondColor.getGreen()
        && secondColor.getBlue() * 2.5 != (double)(secondColor.getBlue()))
            return;
        int red = firstColor.getRed();
        int green = firstColor.getGreen();
        int blue = firstColor.getBlue();
        int diameter = squareSize;
        /*
         * Draws concentric circles starting with a light color and progressively
         * getting darker until the circle is black. If the color does not exist,
         * the code sets the color back to lightColor
         */
        for (int i = 0; i < squareSize/2; i++)
        {
            diameter = squareSize - 2*i; //shifts diameter by 2 pixels each time
            if (red < 0)
            {
                red = lightColor.getRed();
            }
            if (green < 0)
            {
                green = lightColor.getGreen();
            }
            if (blue < 0)
            {
                blue = lightColor.getBlue();
            }
            //set new color and draw new circle
            g.setColor(new Color(red, green, blue));
            g.fillOval(xBlock1 + i, yBlock1 + i, diameter, diameter);
            //values chosen for color scheme
            red -= 10;
            green -= 5;
            blue -= 12;
        } 
        //repeat same process as above for second teleportal
        red = secondColor.getRed();
        green = secondColor.getGreen();
        blue = secondColor.getBlue();
        diameter = squareSize;
        for (int i = 0; i < squareSize/2; i++)
        {
            diameter = squareSize - 2*i;
            if (red < 0)
            {
                red = lightColor.getRed();
            }
            if (green < 0)
            {
                green = lightColor.getGreen();
            }
            if (blue < 0)
            {
                blue = lightColor.getBlue();
            }
            g.setColor(new Color(red, green, blue));
            g.fillOval(xBlock2 + i, yBlock2 + i, diameter, diameter);
            red -= 10;
            green -= 5;
            blue -= 12;
        } 
    }
    
    /*
    * Method would take a lot of work, decided to abandon
    public int distanceBetweenPortals(Maze m)
    {
        int t = m.distanceBetweenPoints(getFirstGridX(), getFirstGridY(), 
                                        getSecondGridX(), getSecondGridY());
        return t;
        //return m.distanceBetweenPoints(getFirstGridX(), getFirstGridY(), 
                                       //getSecondGridX(), getSecondGridY());
    } */
    /**
     * Gives grid x-coordinate of first teleportal based on its pixel location
     * @return integer-value of grid x-coordinate
     */
    public int getFirstGridX()
    {
        return xBlock1/squareSize;
    }
    /**
     * Gives grid y-coordinate of first teleportal based on its pixel location
     * @return integer-value of grid y-coordinate
     */
    public int getFirstGridY()
    {
        return gridHeight - yBlock1/squareSize - 1;
    }
    /**
     * Gives grid x-coordinate of second teleportal based on its pixel location
     * @return integer-value of grid x-coordinate
     */
    public int getSecondGridX()
    {
        return xBlock2/squareSize;
    }
    /**
     * Gives grid y-coordinate of second teleportal based on its pixel location
     * @return integer-value of grid y-coordinate
     */
    public int getSecondGridY()
    {
        return gridHeight - yBlock2/squareSize - 1;
    }
}