import java.io.*;
import java.awt.*;
/**
 * Creates a HighScore object to track the best scores in the game
 * Class currently unused and unfinished due to time constraints
 *
 * @AviRuthen
 * @3/17/21
 */
public class HighScores implements Serializable
{
    String name, date;
    int score, moves;
    int[][][] squareMoves;
    String difficulty = "Medium";
    
    /**
     * Creates a HighScore object with all key info
     * 
     * @param name: name of person who set high score
     * @param score: the score that person set
     * @param date: when the person set score (called in Time.java)
     */
    public HighScores(String name, int score, String date, int moves)
    {
        this.name = name.replace(" ", "");
        this.score = score;
        this.date = date;
        this.moves = moves;
    }
    
    /**
     * Creates a HighScore object with all key info
     * 
     * @param name: name of person who set high score
     * @param score: the score that person set
     * @param date: when the person set score (called in Time.java)
     * @param squareMoves: saves maze generation
     */
    public HighScores(String name, int score, String date, int moves, int[][][] squareMoves)
    {
        this.name = name.replace(" ", "");
        this.score = score;
        this.date = date;
        this.moves = moves;
        this.squareMoves = squareMoves;
    }
    
    /**
     * Creates a HighScore object with all key info
     * 
     * @param name: name of person who set high score
     * @param score: the score that person set
     * @param date: when the person set score (called in Time.java)
     * @param squareMoves: saves maze generation
     * @param difficulty: saves difficulty
     */
    public HighScores(String name, int score, String date, 
                      int moves, int[][][] squareMoves, String difficulty)
    {
        this.name = name.replace(" ", "");
        this.score = score;
        this.date = date;
        this.moves = moves;
        this.squareMoves = squareMoves;
        this.difficulty = difficulty;
    }
    
    /**
     * Neatly puts all information regarding the high score for
     * Adding to text file
     * 
     * @return a String with all info (used in writing HighScores.ser)
     */
    public String getInfo()
    {
        return name + " scored " + score + " at " + date + " in " + moves + " moves on " + difficulty + " mode.";
    }
    
    /**
     * Overrides S.o.p, used for debugging
     * 
     * @return String to be printed
     */
    @Override
    public String toString()
    {
        return name + " scored " + score + " at " + date + " in " + moves + " moves on " + difficulty + " mode.";
    }
    
    /**
     * Adds maze from HighScores object back into graphics window
     */
    public void loadMaze()
    {
        //numSquaresAcross, numSquaresDown, ScreenWidth
        Maze m = new Maze(12, 9, (810/12));
        m.squareMoves = squareMoves;
        m.generateMaze();
        m.findSolution();
    }
}
