import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.*;
import java.awt.Desktop;
import java.net.*;

/**
 * Deals with all methods regarding creating a storing high scores
 *
 * @AviRuthen @3/22/21
 */
public class ScoreProcessor {
    ArrayList<HighScores> scores = new ArrayList<HighScores>();

    /**
     * Prints all the highscores from textfile (used for debugging)
     */
    public static void read() {
        try {
            // Scanner to get information from scores.txt
            FileReader reader = new FileReader("scores.txt");
            Scanner scan = new Scanner(reader);

            // Print information to console
            while (scan.hasNext())
                System.out.println(scan.nextLine());

            scan.close();
        } catch (Exception e) {
            // Catches any exception if file isn't found, etc.
            System.out.println("File missing or not located. IO exception thrown!");
        }
    }

    /**
     * Gets all the scores from the scores.txt file and Stores the scores in an
     * ArrayList of HighScores objects
     * 
     * @return an ArrayList of current high score objects
     */
    public void getScores() {
        // Takes scores from text file
        scores.clear();
        try {
            FileReader reader = new FileReader("scores.txt");
            Scanner scan = new Scanner(reader);
            while (scan.hasNext()) {
                String s = scan.nextLine();
                // Skips over first line
                if (s.equals("High Scores:"))
                    continue;

                // Separate all words in each line
                String[] splits = s.split(" ");

                // splits[2] is the score for each line
                int score = Integer.parseInt(splits[2]);

                String date = "";

                // All words involving the date occurs after splits[2]
                for (int i = 3; i < splits.length - 1; i++)
                    date += splits[i] + " ";

                // Don't add space on last date
                date += splits[splits.length - 1];

                // Assuming order in txt file is name, score, date
                // splits[1] is the name of person (splits[0] is ranking)
                //scores.add(new HighScores(splits[1], score, date));
            }
            scan.close();
        } catch (IOException io) {
            System.out.println("File missing or not located. IO exception thrown!");
        }
    }

    /**
     * Creates a new ArrayList of high score objects with new score added
     * 
     * @return an ArrayList of all current high score objects with new one
     */
    public void add(String name, int score, String date) {
        // Get all the current scores
        getScores();
        // Add the new score
        //scores.add(new HighScores(name, score, date));
    }

    /**
     * Sorts all high score objects by score and saves them To a new text file
     * (scores.txt)
     * 
     * @param the current list of high score objects (easily obtained with the
     *            getScores() method in this class)
     */
    public void saveScore() {
        // Sort scores first by score
        sortScores(scores);

        // When saving, file should not be edited
        // However we are changing this temporarily to add new file
        File file = new File("scores.txt");
        file.setWritable(true);

        // Create new HighScores file, prints all high scores to file
        try {
            PrintWriter writer = new PrintWriter("scores.txt");
            writer.println("High Scores:");
            for (int i = 0; i < scores.size(); i++)
                writer.println((i + 1) + ". " + scores.get(i).getInfo());
            writer.close();
        } catch (IOException io) {
            System.out.println("Error in finding file");
        }
        // Make file uneditable
        file.setWritable(false);
    }

    /**
     * Sorts all objects by score
     * 
     * @param current ArrayList of all high scores
     * @return sorted version of the ArrayList of high scores
     * 
     *         NOTE: Keeping this in to show work
     */
    public void sortScores(ArrayList<HighScores> scores) {
        // I put all the scores in an array so I can use Arrays.sort
        int[] playerScores = new int[scores.size()];

        for (int i = 0; i < scores.size(); i++)
            playerScores[i] = scores.get(i).score;

        // Sort by score
        Arrays.sort(playerScores);

        ArrayList<HighScores> scoreTemp = new ArrayList<HighScores>();

        // Goes in reverse order
        // If HighScores object has same score, adds it to ArrayList
        for (int i = playerScores.length - 1; i >= 0; i--)
            for (HighScores h : scores)
                if (h.score == playerScores[i])
                    scoreTemp.add(h);

        scores.clear();

        for (HighScores h : scoreTemp)
            scores.add(h);
    }
}
