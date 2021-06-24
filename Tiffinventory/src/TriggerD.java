import java.sql.*;

//Assumption - setup() will be called before any other access routine
public class TriggerD {

    private static String[] t_names = {"total_cases", "total_jars", "triggers"};

    
    public static void main(String[] args){
        setup();
        set_trigs(new int[] {3, 4, 5, 6});
        set_totals(ProductEnum.PINK, 10, 4);
        set_totals(ProductEnum.YELLOW, 3, 2);
        set_totals(ProductEnum.ORANGE, 8, 1);
        set_totals(ProductEnum.GREEN, 11, 3);
        int[][] totals = get_totals();
        for(int i = 0 ; i < totals.length ; i ++){
            for(int j = 0 ; j < totals[0].length ; j++){
                System.out.print(totals[i][j] + ", ");
            }
            System.out.println();
        }
        int[] trigs = get_trigs();
        for(int j = 0 ; j < ProductEnum.values().length ; j++){
            System.out.println(trigs[j]);
        }

        
    }
    

    protected static void setup(){
        
        ProductEnum[] names = ProductEnum.values();
        String columns = "(";
        String column_dec = "(";
        String def_vals = "(";
        for(int i = 0 ; i < names.length ; i++){
            column_dec += names[i].toString() + " int NOT NULL";
            columns += names[i].toString();
            if(i < names.length - 1){
                columns += ", ";
                column_dec += ", ";
                def_vals += "0, ";
            }
            else{
                columns += ")";
                column_dec += ")";
                def_vals += "0)";
            }
        }

        Connection con = null;
        Statement stmt = null;

        try{
             con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiffinday", "root", "Brownhawk@11");
             stmt = con.createStatement();

             for(String s : t_names){
                try{
                    stmt.execute("CREATE TABLE " + s + " " + column_dec);
                }
                catch(SQLSyntaxErrorException e){
                    System.out.println(e);
                }

                ResultSet rs = stmt.executeQuery("SELECT EXISTS (SELECT 1 FROM " + s + ")");
                rs.next();
                if (rs.getInt("EXISTS (SELECT 1 FROM " + s + ")") == 1){
                    continue;
                }
                stmt.executeUpdate("INSERT INTO " + s + " " + columns + " VALUES " + def_vals);
             
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

    public static int[][] get_totals(){
        Connection con = null;
        Statement stmt = null;
        int[][] prod = new int[2][ProductEnum.values().length];

        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiffinday", "root", "Brownhawk@11");
            stmt = con.createStatement();
            
            for(int i = 0 ; i < 2 ; i++){ //2 is the number of different product types
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + t_names[i]);
                rs.next();

                for(ProductEnum p : ProductEnum.values()){
                    prod[i][p.get_ind()] = rs.getInt(p.toString());
                }
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
            catch(Exception e){
            }
        }
        return prod; // - [[total cases of each type], [total jars of each type]]
    }

    //Assumption: values passed will be valid (Natural numbers)
    protected static void set_totals(ProductEnum p, int new_c, int new_j){ 
        Connection con = null;
        Statement stmt = null;

        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiffinday", "root", "Brownhawk@11");
            stmt = con.createStatement();

            stmt.executeUpdate("UPDATE " + t_names[0] + " SET " + p.toString() + " = " + Integer.toString(new_c));
            stmt.executeUpdate("UPDATE " + t_names[1] + " SET " + p.toString() + " = " + Integer.toString(new_j));
        }

        catch(SQLException e){
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

    public static int[] get_trigs(){
        Connection con = null;
        Statement stmt = null;
        int[] trigs = new int[ProductEnum.values().length];

        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiffinday", "root", "Brownhawk@11");
            stmt = con.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + t_names[2]);
            rs.next();

            for(ProductEnum p : ProductEnum.values()){
                trigs[p.get_ind()] = rs.getInt(p.toString());
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
            catch(Exception e){
            }
        }
        return trigs;
    }

    public static void set_trigs(int[] trigs){
        
        ProductEnum[] names = ProductEnum.values();
        if(trigs.length != names.length){
            System.out.println("Poor trigger formatting");
            return;
        }
    
        String set = "";
        
        for(int i = 0 ; i < names.length ; i++){

            set += names[i].toString() + " = " + Integer.toString(trigs[i]);

            if(i < names.length - 1){
                set += ", ";
            }
        }
        
        Connection con = null;
        Statement stmt = null; 

        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiffinday", "root", "Brownhawk@11");
            stmt = con.createStatement();
            stmt.executeUpdate("UPDATE " + t_names[2] + " SET " + set);
        }

        catch(SQLException e){
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
}
