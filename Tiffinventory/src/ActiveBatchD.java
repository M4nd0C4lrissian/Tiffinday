import java.sql.*;
import java.util.ArrayList;
public class ActiveBatchD {
    public static void main(String[] agrs){
        try{
             //Class.forName("com.mysql.cj.jdbc.Driver"); -unneeded
             Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "Brownhawk@11");
             Statement stmt = con.createStatement();
             for(int i = 0 ; i < ProductEnum.values().length ; i++){ //primary key may have to be reworked
                stmt.execute("CREATE TABLE " + Integer.toString(i) + " (DateMade varchar(10) NOT NULL PRIMARY KEY, Cases int, Jars int");  //currently assumes two batches of the same type won't be made on the same day
             }
             stmt.close();
             con.close();
        }
       // catch(Exception sQLSyntaxErrorException){}
        
        catch(Exception e){
            System.out.println(e);
        }
    }

    public static void new_batch(BatchT to_add){ //assumes good date values -- should be BatchT invariant
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "Brownhawk@11");
            Statement stmt = con.createStatement();
            int[] date = to_add.getDate();
            String s_date = "";
            for(int i = 0 ; i < 3 ; i++){
                s_date += Integer.toString(date[i]);
                if(i < 2){
                    s_date += Services.DATE_DELIM;
                }
            }
            stmt.execute("INSERT INTO " + Integer.toString(to_add.getProd().get_ind()) + " (DateMade, Cases, Jars) VALUES (" + s_date + ", " 
            + Integer.toString(to_add.getCnum()) + ", " + Integer.toString(to_add.getJnum()) + ")");
        }

        catch(SQLException e){
            System.out.println(e);
        }
    }

    public static ArrayList<BatchT> getBatches(ProductEnum p){
        ArrayList<BatchT> batches = new ArrayList<BatchT>();
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "Brownhawk@11");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + Integer.toString(p.get_ind()));
            while(rs.next()){
                String s_date = rs.getString("DateMade");
                String[] s_date_ar = s_date.split(Services.DATE_DELIM, 3);
                int[] date = new int[3];
                for(int i = 0 ; i < 3 ; i++){
                    try{
                        date[i] = Integer.parseInt(s_date_ar[i]);
                    }

                    catch(NumberFormatException e){ //OR -- catch this error in new_batch --- so we know table date values are safe --should really be handled in BatchT constructor
                        ////do something here -- print what is written and give the other info to identify the batch and ask user to rewrite date, then remember to update the 
                        //database -- *could instead implement with year, month, day integer columns
                    }
                }
                batches.add(new BatchT(p, date , rs.getInt("Cases"), rs.getInt("Jars)")));
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return batches; //remember to quicksort output
    }


    
}
