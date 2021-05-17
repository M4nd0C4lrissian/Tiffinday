public interface Batch {

    public final int CASE_SIZE = 12;

    public final int EXP_RANGE = 1; //1 year expiration dates

    public ProductEnum getProd();

    public String getNote();

    public void write_note(String s); //note on shipment, not batch

    public int[] getDate();

    public int[] getDate(int index); //for multiBatch - assumption is that batches will be indexable (using ArrayList)

    public void make_case();

    public void split_case();

    public int getCnum();
    
    public int getJnum();

    public void ship_case(int c);

    public void ship_jar(int j);

    public boolean is_empty();

    public boolean is_expired();

    public void ship_fromDate(String date); //idk about this one

    //public Shipment getShip();

    //Multibatch needs 
}
