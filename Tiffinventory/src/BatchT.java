import java.util.InputMismatchException;
//**NOTE: state invariant : c_num and j_num >= 0 for all time. 
public class BatchT{ //implements Batch{
    private final ProductEnum prod;
    private int[] date;
    //private String note;
    private int c_num;
    private int j_num;
    private ShipmentT sent = new ShipmentT(); 

    //have to have it interface with total? - maybe that'll be handled at greater level of hierarchy

    public BatchT(ProductEnum p, int[] date_made, int cases, int  jars) throws InputMismatchException{
        this.prod = p;
        this.c_num = cases;
        this.j_num = jars;
        if(!(is_validDate(date_made)) || cases < 0 || jars < 0){
            throw new InputMismatchException("Poor date format and/or invalid case or jar input (must be positive)."); //ward against and provide regular non-error message in UI
        }
        this.date = date_made;
    }

    public ProductEnum getProd(){
        return prod;
    }

    public void write_note(String s){ //note should be on shipment, not batch****
        //note = s;
    }

   // public String getNote(){
        //return note;
    //}

    public int[] getDate(){
        return date;
    }

    public int[] getDate(int index){
        return date;
    }

    public void make_case(){
        if(j_num >= Services.CASE_SIZE){ //create constants module to replace Batch
            c_num++;
            j_num--;
            return;
        }
        System.out.println("Not enough jars to form a case");
    }

    public void split_case(){
        if(c_num >= 1){
            j_num += Services.CASE_SIZE;
            c_num--;
            return;
        }
        System.out.println("No cases available to split");
    }

    public int getCnum(){
        return c_num;
    }

    public int getJnum(){
        return j_num;
    }

    public boolean is_empty(){ //might be useless
        return (c_num <= 0 && j_num <= 0);
    }

    public boolean is_expired(){
       String[] c_date = java.time.LocalDate.now().toString().split("-");
       int[] exp = date;
       exp[0] += Services.EXP_RANGE; //assumes expiration is just on the year -- we should make and treat expiration similarly to 
       for(int i = 2 ; i >= 0 ; i-- ){
           if(exp[i] < Integer.parseInt(c_date[i])){
               return true;
           }
       }
       return false;
    }

    public void ship(int c, int j, String place){ 
        if(c_num >= c && j_num >= j){
            sent.add_ship(c, j, place);
            c_num = c_num - c;
            j_num = j_num - j;
            //update trigger? - answer : NO - covered in BatchQueue as expected given UML
        }

        else{
            System.out.println("Not enough cases / jars available to ship this amount. There are " + Integer.toString(c_num) + " cases and " + Integer.toString(j_num) + " jars available.");
        } //this error should be warded against in BatchQueue, so maybe take out?
    }


    //**MOVE TO BATCHQUEUE**
    protected void override(int new_c, int new_j){ //might have to be implemented in BatchQueue, using date search to find particular batch -- because it has to talk to database
        if(new_c < 0 || new_j < 0){
            ///ERROR  
        }
        Trigger.decr_prod(prod, c_num - new_c , j_num - new_j);
        c_num = new_c;
        j_num = new_j;
    }

    private static boolean is_validDate(int[] date){
        if(date.length != 3){
            return false;
        }

        if(Integer.toString(date[0]).length() != 4){
            return false;
        }

        if(Integer.toString(date[1]).length() != 2){
            return false;
        }

        if(Integer.toString(date[2]).length() != 2){
            return false;
        }

        return true;
    }
}

//Gonna be a bitch to always treat case and jar as separate and specified entities if we wish to generalize. Should do something similar to my OG CaseT and JarT, but with just enums, and their associated values 
//--Note, associated values wouldn't work because they need to be dynamic, but it could work with database, initialized by user, to create a "relatively final" array (arraylist most likely) of strings associated to indices that can be used as reference (so when they click on the screen to 
// to choose the specific product, that will be associated with the respective indice as well), and we can pass in an array of [0,2,3,1,8, ... , 1] lets say, and that would override the values for all N types of sizes / packaging methods associated with those indices, and another boolean array or just arraylist of ints could be passed to indicate where and where not to apply the function)
