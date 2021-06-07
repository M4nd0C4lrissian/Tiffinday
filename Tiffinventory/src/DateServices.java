import java.util.InputMismatchException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class DateServices {
    public static boolean is_validDate(int[] date){
        DateValidator d = new DateValidator("yyyy/MM/dd");
        return d.isValid(date_ArrtoString(date));
    }

    public static String date_ArrtoString(int[] date) throws InputMismatchException{ //assumes length of date is correct -- ensure that it is (i think this will be handled fine) -- at this point may have been worth it to just make date datatype
        if(date.length != 3){
            throw new InputMismatchException("Poor date array length");
        }
        String s_date = "";
            for(int i = 0 ; i < 3 ; i++){
                s_date += Integer.toString(date[i]);
                if(i < 2){
                    s_date += Constants.DATE_DELIM;
                }
            }
        return s_date;
    }

    public static void main(String[] args){
        int[] bad_date = {1999,10,2};
        System.out.println(date_ArrtoString(bad_date));
        System.out.println(is_validDate(bad_date));
    }

    private static class DateValidator{
        private String dateFormat;
    
        public DateValidator(String dateFormat) {
            this.dateFormat = dateFormat;
        }
    
        public boolean isValid(String dateStr) {
            DateFormat sdf = new SimpleDateFormat(this.dateFormat);
            sdf.setLenient(false);
            try {
                sdf.parse(dateStr);
            } catch (ParseException e) {
                return false;
            }
            return true;
        }
    }
}
