import java.util.ArrayList;
import java.util.InputMismatchException;

public class Services {
    public static final int CASE_SIZE = 12;
    public static final int EXP_RANGE = 1;

    //tested -- might make it return indices instead so we dont have to pass around arraylists - although probably just passes mem location but still faster with ints
    //so possibility for improvement
    public static ArrayList<BatchT> insertbyDate(ArrayList<BatchT> batches, BatchT to_add) throws InputMismatchException{
        
        int[] s_date = to_add.getDate();

        int lo = 0;
        int hi = batches.size() - 1;

        if(is_later(batches.get(lo).getDate(), s_date)){
            batches.add(lo, to_add);
            return batches;
        }

        if(is_later(s_date, batches.get(hi).getDate())){
            batches.add(to_add);
            return batches;
        }
        while(hi != lo){ //this method will require testing
            int mid = (hi + lo) / 2; 
            if(is_later(s_date, batches.get(mid).getDate())){
                lo = mid + 1;
                if(is_later(batches.get(lo).getDate(), s_date)){
                    batches.add(lo, to_add);
                    return batches;
                }
            }
            else{
                hi = mid - 1;
            }
            
        }
        if(lo + 1 >= batches.size()){
            batches.add(to_add);
        }

        batches.add(lo + 1, to_add);
        return batches;
        
    }

    private static boolean is_later(int[] l_date, int[] e_date) throws InputMismatchException{ //this could be put in constants / services module for use in is_expired with EXP range passed as a length three int[]
        if(l_date.length != 3 || l_date.length != e_date.length){
            throw new InputMismatchException("Invalid date format");
        }

        for(int i = 0 ; i < l_date.length ; i++){
            if(l_date[i] > e_date[i]){
                return true;
            }
            else if(l_date[i] == e_date[i]){
                continue;
            }
            else{
                return false;
            }
        }

        return true; // if they're equal we say sure it is later
    }
    public static void main(String[] args){
        ArrayList<BatchT> batches = new ArrayList<BatchT>(); 
        batches.add(new BatchT(ProductEnum.GREEN, new int[] {2001, 2, 20}, 4, 4));
        batches.add(new BatchT(ProductEnum.GREEN, new int[] {2001, 4, 20}, 4, 4));
        batches.add(new BatchT(ProductEnum.GREEN, new int[] {2003, 5, 16}, 4, 4));
        batches.add(new BatchT(ProductEnum.GREEN, new int[] {2007, 4, 20}, 4, 4));
        batches.add(new BatchT(ProductEnum.GREEN, new int[] {2011, 3, 30}, 4, 4));
        batches.add(new BatchT(ProductEnum.GREEN, new int[] {2018, 3, 30}, 4, 4));

        BatchT to_add = new BatchT(ProductEnum.GREEN, new int[] {2021, 4, 18}, 4, 4);
        batches = insertbyDate(batches, to_add);

        for(BatchT b : batches){
            print(b);
        }
    }

    public static void print(BatchT to_print){
        int[] date = to_print.getDate();
        for(int i : date){
            System.out.print(Integer.toString(i) + "/");
        }
        System.out.print(", ");
    }
}
