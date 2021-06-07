import java.sql.*;
import java.util.ArrayList;
public class ActiveBatchD {
    public static void main(String[] agrs){ //this should be initialized in some setup process??
        try{
             //Class.forName("com.mysql.cj.jdbc.Driver"); -unneeded
             Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "Brownhawk@11");
             Statement stmt = con.createStatement();
             for(int i = 0 ; i < ProductEnum.values().length ; i++){ //primary key may have to be reworked
                try{
                    stmt.execute("CREATE TABLE " + Integer.toString(i) + " (DateMade varchar(10) NOT NULL, Cases int NOT NULL, Jars int NOT NULL");  
                }
                catch(SQLSyntaxErrorException e){
                    continue;
                }
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

            ResultSet rs = stmt.executeQuery("SELECT * FROM " + Integer.toString(to_add.getProd().get_ind()) +" WHERE DateMade = " + DateServices.date_ArrtoString(to_add.getDate()));
            int size = 0;
            if(rs.last()){
                size = rs.getRow();
            }
            if(size == 1){ //because we check for it upon every addition, we can treat this as a 0 or 1, either one already exists, or none exist
                int c = rs.getInt("Cases");
                int j = rs.getInt("Jars");
                stmt.executeQuery("UPDATE " + Integer.toString(to_add.getProd().get_ind()) + " SET Cases = " 
                + Integer.toString(to_add.getCnum() + c) + ", Jars = " + Integer.toString(to_add.getJnum() + j) + " WHERE DateMade = " + DateServices.date_ArrtoString(to_add.getDate()));
                stmt.close();
                con.close();
                return; //manual update instead of passing to change_vals as change vals will accept only BatchT objects to ensure correct input values and this method does not have access
                //to the source BatchT object that needs its values changed
            }

            stmt.execute("INSERT INTO " + Integer.toString(to_add.getProd().get_ind()) + " (DateMade, Cases, Jars) VALUES (" + DateServices.date_ArrtoString(date) + ", " 
            + Integer.toString(to_add.getCnum()) + ", " + Integer.toString(to_add.getJnum()) + ")");

            stmt.close();
            con.close();
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
                String[] s_date_ar = s_date.split(Constants.DATE_DELIM, 3); 
                int[] date = new int[3];                                    
                for(int i = 0 ; i < 3 ; i++){
                    date[i] = Integer.parseInt(s_date_ar[i]);
                }
                batches.add(new BatchT(p, date , rs.getInt("Cases"), rs.getInt("Jars)")));
            }
            stmt.close();
            con.close();
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return batches; //remember to quicksort output
    }

    public static void del_batch(BatchT to_del){ //have luxury that each table will never have different batches under any given
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "Brownhawk@11");
            Statement stmt = con.createStatement();
            int[] date = to_del.getDate();

            stmt.execute("DELETE FROM " + Integer.toString(to_del.getProd().get_ind()) + " WHERE DateMade = " + DateServices.date_ArrtoString(date));

            stmt.close();
            con.close();
        }

        catch(SQLException e){
            System.out.println(e);
        }
    }

    public static void decr_vals(BatchT to_change, int d_c, int d_j){ //maybe should be decr values -- easier and more common to decrease
        int new_c = to_change.getCnum() - d_c; //these not being less than 0 should be handled in BatchQueue - make sure they are
        int new_j = to_change.getJnum() - d_j;

        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "Brownhawk@11");
            Statement stmt = con.createStatement();
            int[] date = to_change.getDate();

            stmt.executeQuery("UPDATE " + Integer.toString(to_change.getProd().get_ind()) + " SET Cases = " 
            + Integer.toString(new_c) + ", Jars = " + Integer.toString(new_j) + " WHERE DateMade = " + DateServices.date_ArrtoString(date));
            stmt.close();
            con.close();
        }

        catch(SQLException e){
            System.out.println(e);
        }
    }

    
}
