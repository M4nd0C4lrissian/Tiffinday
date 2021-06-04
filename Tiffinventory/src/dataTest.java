import java.sql.*;
public class dataTest {
    public static void main(String[] agrs){
       try{
            //Class.forName("com.mysql.cj.jdbc.Driver"); -unneeded
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "Brownhawk@11");
            Statement stmt = con.createStatement();
            if(stmt.execute("CREATE TABLE test_table (Poopy int)")){
                stmt.executeQuery("CREATE TABLE test_table (Poopy int)");
            }
            stmt.close();
            con.close();
       }
       catch(Exception e){
            System.out.println(e);
       }
    }
}
