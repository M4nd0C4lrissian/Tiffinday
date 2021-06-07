import java.util.ArrayList;
//***NOTE***: all "string warnings", should and shall be replaced with errors, as designated henceforth by mighty King Earl, first of his name.  

public class BatchQueue {  //responsible for tracking active batches, for making shipments thoughtless and providing info for the table to be made
    protected static ArrayList<ArrayList<BatchT>> batches = new ArrayList<ArrayList<BatchT>>(ProductEnum.values().length); //as this will pull from ActiveBatchD, we need to check for 0 length, it could mean that there arent any batches of that kind or there is an issue with the database (because of try-catch in getBatches)
    
    public static void new_batch(ProductEnum p, String date, int c, int j){

    }
    

    public static void ship(ProductEnum p, String place, int c, int j){  //this must be tested independently before database integration
        ArrayList<BatchT> s_type = batches.get(p.get_ind());
        int c2ship = c;
        int j2ship = j;
        int q = 0;
        while(c2ship > 0 && j2ship > 0){
            if(s_type.size() == 0){
                System.out.println("Not enough stock. " + Integer.toString(c) + " cases and " + Integer.toString(j) + " jars of type " + p.toString() + " were unable to be marked for distribution to " + place + ".");
                break;
            }
            BatchT b = s_type.get(q);
            if (b.is_expired()){
                s_type.remove(q);
                System.out.println("The Batch of type " + p.toString() + " created on " + b.getDate() + " has expired. It will no longer be considered for shipment.");
                continue;
            }
            int c_num = b.getCnum();
            int j_num  = b.getJnum();
            if(c_num >= c2ship){
                c2ship = 0;
                if(j_num >= j2ship){
                    b.ship(c2ship, j2ship, place); //note : values must manually be set to 0, as negative values for j2ship and c2ship would break operations in other modules
                    j2ship = 0;                    //namely BatchT.ship()
                }
                else{
                    b.ship(c2ship, j_num, place);
                    j2ship -= j_num;
                }
            }
            else{
                c2ship -= c_num;
                if(j_num >= j2ship){
                    b.ship(c_num, j2ship, place);
                    j2ship = 0;
                }
                else{
                    b.ship(c_num, j_num, place);
                    j2ship -= j_num;
                }
            }
            if(b.is_empty()){
                s_type.remove(q);
                continue;
            }
            q++;
        }
        Trigger.decr_prod(p, c - c2ship, j - j2ship);
    }

  //  public void remove_batch(int index); //will take in date and will use similar principal to searching for add_batch_at_date. (searching in ordered array so binary search but with a few extra compares)


    //implement override
}

//delete batch


//this method must also write to database and read from? (it should pull current ArrayList from data whenever program is started) -- need to ensure that we ward against 
//data hazards***


