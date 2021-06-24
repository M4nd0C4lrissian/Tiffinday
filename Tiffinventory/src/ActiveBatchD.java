import java.sql.*;
import java.util.ArrayList;

//Assumption - setup() will be called before any other access routine
public class ActiveBatchD {
    public static void main(String[] agrs){
        setup();
             //add some stuff !!!!
            // BatchT b = new BatchT(ProductEnum.YELLOW, new int[] {2021, 06, 18}, 10, 3);
            // BatchT b2 = new BatchT(ProductEnum.YELLOW, new int[] {2021, 05, 18}, 10, 3);
            // new_batch(b);
            // new_batch(b2);
            // del_batch(b); 
            // new_batch(new BatchT(ProductEnum.YELLOW, new int[] {2019, 03, 12}, 5, 2));
            // new_batch(new BatchT(ProductEnum.YELLOW, new int[] {2019, 07, 9}, 4, 8));
            // new_batch(new BatchT(ProductEnum.YELLOW, new int[] {2020, 12, 30}, 10, 2));
            //decr_vals(b2, 7, 2);
      
    }

    public static void setup(){
        Connection con = null;
        Statement stmt = null;
        try{
             //Class.forName("com.mysql.cj.jdbc.Driver"); -unneeded
             con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiffinday", "root", "Brownhawk@11");
             stmt = con.createStatement();
             for(int i = 0 ; i < ProductEnum.values().length ; i++){ //primary key may have to be reworked
                try{
                    stmt.execute("CREATE TABLE table_" + Integer.toString(i) + " (Year int NOT NULL, Month int NOT NULL, Day int NOT NULL, Cases int NOT NULL, Jars int NOT NULL)");  
                } 
                catch(SQLSyntaxErrorException e){
                    System.out.println(e);
                    continue;
                }
            }
        }
        
        catch(Exception e){
            System.out.println(e);
        }

        finally{
            try{
                stmt.close();
                con.close();
            }
            catch(Exception e){
            }
        }
    }

    public static void new_batch(BatchT to_add){ //assumes good date values -- should be BatchT invariant
        Connection con = null;
        Statement stmt = null;
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiffinday", "root", "Brownhawk@11");
            stmt = con.createStatement();
            int[] date = to_add.getDate();

           /* ResultSet rs = stmt.executeQuery("SELECT * FROM table_" + Integer.toString(to_add.getProd().get_ind()) + " WHERE (Year = " + Integer.toString(date[0]) + 
            ") AND (Month = " + Integer.toString(date[1]) + ") AND (Day = " + Integer.toString(date[2]) + ")");
            int size = 0;
            if(rs.last()){
                size = rs.getRow();
            }
            if(size >= 1){ //this is now unnecessary as it is handled in BatchQueue prior to insertion
                int c = rs.getInt("Cases");
                int j = rs.getInt("Jars");
                stmt.executeUpdate("UPDATE table_" + Integer.toString(to_add.getProd().get_ind()) + " SET Cases = " 
                + Integer.toString(to_add.getCnum() + c) + ", Jars = " + Integer.toString(to_add.getJnum() + j) + " WHERE (Year = " + Integer.toString(date[0]) + 
                ") AND (Month = " + Integer.toString(date[1]) + ") AND (Day = " + Integer.toString(date[2]) + ")");
                stmt.close();
                con.close();
                return; //manual update instead of passing to change_vals as change vals will accept only BatchT objects to ensure correct input values and this method does not have access
                //to the source BatchT object that needs its values changed
            }
            */

            stmt.executeUpdate("INSERT INTO table_" + Integer.toString(to_add.getProd().get_ind()) + " (Year, Month, Day, Cases, Jars) VALUES (" + Integer.toString(date[0]) + ", " 
            + Integer.toString(date[1]) + ", " + Integer.toString(date[2]) + ", " + Integer.toString(to_add.getCnum()) + ", " + Integer.toString(to_add.getJnum()) + ")");
        }

        catch(SQLException e){
            System.out.println(e);
        }

        finally{
            try{
                stmt.close();
                con.close();
            }
            catch(Exception e){}
        }
    }

    public static ArrayList<BatchT> getBatches(ProductEnum p){
        ArrayList<BatchT> batches = new ArrayList<BatchT>();
        Connection con = null;
        Statement stmt = null;
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiffinday", "root", "Brownhawk@11");
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM table_" + Integer.toString(p.get_ind()));
            while(rs.next()){
                int[] date = new int[] {rs.getInt("Year"), rs.getInt("Month"), rs.getInt("Day")};                                    
                batches.add(new BatchT(p, date , rs.getInt("Cases"), rs.getInt("Jars")));
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }

        finally{
            try{
                stmt.close();
                con.close();
            }
            catch(Exception e){}
        }

        return batches; //remember to quicksort output
    }

    public static void del_batch(BatchT to_del){ //have luxury that each table will never have different batches under any given
        Connection con = null;
        Statement stmt = null;
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiffinday", "root", "Brownhawk@11");
            stmt = con.createStatement();
            int[] date = to_del.getDate();

            stmt.executeUpdate("DELETE FROM table_" + Integer.toString(to_del.getProd().get_ind()) + " WHERE (Year = " + Integer.toString(date[0]) + 
            ") AND (Month = " + Integer.toString(date[1]) + ") AND (Day = " + Integer.toString(date[2]) + ")");
        }

        catch(SQLException e){
            System.out.println(e);
        }

        finally{
            try{
                stmt.close();
                con.close();
            }
            catch(Exception e){}
        }
    }

    //Note: passing 0s here will update an already decremented Batch in BatchQueue (like in ship()) 
    public static void decr_vals(BatchT to_change, int d_c, int d_j){ //maybe should be decr values -- easier and more common to decrease
        int new_c = to_change.getCnum() - d_c; //these not being less than 0 should be handled in BatchQueue - make sure they are
        int new_j = to_change.getJnum() - d_j;

        Connection con = null;
        Statement stmt = null;
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiffinday", "root", "Brownhawk@11");
            stmt = con.createStatement();
            int[] date = to_change.getDate();
            stmt.executeUpdate("UPDATE table_" + Integer.toString(to_change.getProd().get_ind()) + " SET Cases = " 
            + Integer.toString(new_c) + ", Jars = " + Integer.toString(new_j) + " WHERE (Year = " + Integer.toString(date[0]) + 
            ") AND (Month = " + Integer.toString(date[1]) + ") AND (Day = " + Integer.toString(date[2]) + ")");
        }

        catch(SQLException e){
            System.out.println(e);
        }

        finally{
            try{
                stmt.close();
                con.close();
            }
            catch(Exception e){}
        }
    }

    
}
