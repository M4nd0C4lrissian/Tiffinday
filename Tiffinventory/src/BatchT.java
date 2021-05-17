import java.util.InputMismatchException;

public class BatchT{ //implements Batch{
    private final ProductEnum prod;
    private int[] date;
    private String note;
    private int c_num;
    private int j_num;
    //private Shipment out; - have to make associated shipment object / type

    //have to have it interface with total? - maybe that'll be handled at greater level of hierarchy

    public BatchT(ProductEnum p, int[] date_made, int cases, int  jars) throws InputMismatchException{
        this.prod = p;
        this.c_num = cases;
        this.j_num = jars;
        if(date_made.length != 3){
            throw new InputMismatchException("Poor date format.");
        }
        this.date = date_made;
    }

    public ProductEnum getProd(){
        return prod;
    }

    public void write_note(String s){ //note should be on shipment, not batch
        note = s;
    }

    public String getNote(){
        return note;
    }

    public int[] getDate(){
        return date;
    }

    public int[] getDate(int index){
        return date;
    }

    public void make_case(){
        if(j_num >= Batch.CASE_SIZE){
            c_num++;
            j_num--;
            return;
        }
        System.out.println("Not enough jars to form a case");
    }

    public void split_case(){
        if(c_num >= 1){
            j_num += Batch.CASE_SIZE;
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
       exp[0] += Batch.EXP_RANGE;
       for(int i = 2 ; i >= 0 ; i-- ){
           if(exp[i] < Integer.parseInt(c_date[i])){
               return true;
           }
       }
       return false;
    }

    //TODO
    public void ship(int c, int j){
        return;
    }

    protected void c_over(int new_c){
        c_num = new_c;
    }

    protected void j_over(int new_j){ 
        j_num = new_j;
    }
}