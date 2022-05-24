import java.util.Date;
import java.text.DateFormat; 
import java.util.Calendar;
/**
 * Used to get computer time for when score is set
 *
 * @Avi Ruthen
 * @3/22/21
 */
public class Time
{
    public static String getTime()
    {
        //makes an object with today's date/time info:
      Date now = new Date();
   
      //make a calendar object to be able to get data from:
      Calendar cal = Calendar.getInstance();
      //set the calendar to now:
      cal.setTime(now);
      
      return DateFormat.getDateTimeInstance().format(now);  
    }
}
