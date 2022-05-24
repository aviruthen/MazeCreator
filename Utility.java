/**
 * Used for a few extraneous methods
 *
 * @AmarRuthen
 * @5/25/21
 */
public class Utility
{
    public Utility()
    {

    }
    /**
     * Returns a random integer up to the maximum value
     * @param max Maximum value
     * @return Random integer
     */
    public int randomInteger(int max)
    {
        return (int)((max+1)*Math.random());
    }
    /**
     * Returns a random integer from a minimum to a maximum value inclusive
     * @param min Minimum value
     * @param max Maximum value
     * @return Random integer
     */
    public int randomInteger(int min, int max)
    {
        return (int)((max+1)*Math.random()) + min;
    }
    /**
     * Returns the floor of a double as an integer
     * @param a The number to floor
     * @return The floor of that number
     */
    public int floor(double a)
    {
        return (int)(Math.floor(a));
    }
    /**
     * Returns the ceiling of a double as an integer
     * @param a The number to take the ceiling
     * @return The ceiling of that number
     */
    public int ceil(double a)
    {
        return (int)(Math.ceil(a));
    }
}