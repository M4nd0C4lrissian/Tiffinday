import java.util.ArrayList;
import java.util.InputMismatchException;

public class BatchQueue {  //responsible for tracking active batches, for making shipments thoughtless and providing info for the table to be made
    protected static ArrayList<ArrayList<BatchT>> batches = new ArrayList<ArrayList<BatchT>>(ProductEnum.values().length); //as this will pull from ActiveBatchD, we need to check for 0 length, it could mean that there arent any batches of that kind or there is an issue with the database (because of try-catch in getBatches)


    public static void new_batch(ProductEnum p, int[] date, int c, int j){
        try{
            BatchT b = new BatchT(p, date, c, j);
            BatchSorter.insertbyDate(batches.get(p.get_ind()), b);
            Trigger.decr_prod(p, -c, -j);
            ActiveBatchD.new_batch(b);
        }
        catch(InputMismatchException e){
            System.out.println("Incorrect date format / case and jar values");
            return;
        }
    }

    protected static void setup(){
        for(int i = 0 ; i < ProductEnum.values().length ; i++){ //IMPORTANT** this should always be initialized -- actually won't need to be because will pull from DB every time
            batches.add(ActiveBatchD.getBatches(ProductEnum.values()[i]));
            BatchSorter.quickSort(batches.get(i));
        }
    }
    
    /*
    public static void main(String[] args){
        setup();

        new_batch(ProductEnum.GREEN, new int[] {2020, 06, 14}, 2, 6);
        new_batch(ProductEnum.GREEN, new int[] {2021, 06, 17}, 10, 2);
        new_batch(ProductEnum.GREEN, new int[] {2021, 07, 17}, 9, 3);

        remove_batch_by_Date(ProductEnum.GREEN, new int[] {2021, 07, 17});
        //ship(ProductEnum.GREEN, "yo mama", 4, 5);
       // for(int i = 0 ; i < ProductEnum.values().length ; i++){
            int i = 1;
            System.out.print("Batches in " + Integer.toString(i) + ": ");
            for(BatchT b : batches.get(i)){
                printBatch(b);
            }
            System.out.println();
            System.out.print("Total : " + Integer.toString(Trigger.total_c[i]) + " cases and " + Integer.toString(Trigger.total_j[i]) + " jars.");
            System.out.println();
        //}

        System.out.println();
        for(BatchT b : batches.get(0)){
            printBatch(b);
            System.out.println();
        }

    }
    */

    public static void disp_batches(ProductEnum p){
        for(BatchT b : batches.get(p.get_ind())){
            printBatch(b);
        }
    }

    public static void printBatch(BatchT b){
        BatchSorter.print(b);
        System.out.print("c : " + Integer.toString(b.getCnum()) + ", j : " + Integer.toString(b.getJnum()) + ", ");
    }

    //TO TEST
    public static void ship(ProductEnum p, String place, int c, int j){  //this must be tested independently before database integration
        check_exp(p);
        if(Trigger.total_c[p.get_ind()] < c || Trigger.total_j[p.get_ind()] < j){
            System.out.println("Not enough stock. " + Integer.toString(c) + " cases and " + Integer.toString(j) + " jars of type " + p.toString() + 
            " were requested for distribution to " + place + ". However, only " + Integer.toString(Trigger.total_c[p.get_ind()]) + " cases and "
            + Integer.toString(Trigger.total_j[p.get_ind()]) + " jars of this type are available.");
            return;
        }

        ArrayList<BatchT> s_type = batches.get(p.get_ind());
        int c2ship = c;
        int j2ship = j;
        int q = 0;

        while((c2ship > 0 || j2ship > 0) && q < s_type.size()){ // and condition can probably be removed once everything is shown to be working fine
            BatchT b = s_type.get(q);
          
            int c_num = b.getCnum();
            int j_num  = b.getJnum();
            if(c_num >= c2ship){
                if(j_num >= j2ship){
                    b.ship(c2ship, j2ship, place); 
                    j2ship = 0;                    
                }
                else{
                    b.ship(c2ship, j_num, place);
                    j2ship -= j_num;
                }
                c2ship = 0;
            }
            else{
                if(j_num >= j2ship){
                    b.ship(c_num, j2ship, place);
                    j2ship = 0;
                }
                else{
                    b.ship(c_num, j_num, place);
                    j2ship -= j_num;
                }
                c2ship -= c_num;
            }

            ActiveBatchD.decr_vals(b, c - c2ship, j - j2ship);

            if(b.is_empty()){
                ActiveBatchD.del_batch(b);
                s_type.remove(q);
                continue;
            }
            q++;
        }
        Trigger.decr_prod(p, c - c2ship, j - j2ship);
    }

    
    public static void remove_batch_by_Date(ProductEnum p, int[] to_del) throws InputMismatchException{ //will take in date and will use similar principal to searching for add_batch_at_date. (searching in ordered array so binary search but with a few extra compares)
        if(!DateServices.is_validDate(to_del)){
            System.out.println("Invalid date format");
            return;
        }
        ArrayList<BatchT> batch_q = batches.get(p.get_ind());

        int ind = BatchSorter.findbyDate(batch_q, 0, batch_q.size() - 1, to_del);
        if (ind < 0){
            System.out.println("No such active batch of type " + p.toString() + " was created on " + DateServices.date_ArrtoString(to_del));
            return;
        }
        BatchT d = batch_q.get(ind);
        int d_c = d.getCnum();
        int d_j = d.getJnum();
        batch_q.remove(ind); 
        Trigger.decr_prod(p, d_c, d_j);
        ActiveBatchD.del_batch(d);
    }

    public static void remove_batch_by_ind(ProductEnum p, int ind){
        ArrayList<BatchT> batch_q = batches.get(p.get_ind());
        if(ind >= batch_q.size()){
            System.out.println("Invalid input index.");
            return;
        }
        BatchT d = batch_q.get(ind);
        int d_c = d.getCnum();
        int d_j = d.getJnum();

        batch_q.remove(ind); 
        Trigger.decr_prod(p, d_c, d_j);
        ActiveBatchD.del_batch(d);
    }

    //implement override


    private static void check_exp(ProductEnum p){
        ArrayList<BatchT> batch_q = batches.get(p.get_ind());
        if(batch_q.isEmpty()){
            return;
        }
        BatchT b = batch_q.get(0);
        while(b.is_expired()){
            remove_batch_by_ind(p, 0);
            System.out.println("The batch of type " + p.toString() + " created on " + DateServices.date_ArrtoString(b.getDate()) + " has expired.");
            if(batch_q.isEmpty()){
                break;
            }
            b = batch_q.get(0);
        }
    }
}




