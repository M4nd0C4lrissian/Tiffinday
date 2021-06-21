import java.util.ArrayList;

public class BatchSorter {

    //tested
    public static void insertbyDate(ArrayList<BatchT> batches, BatchT to_add){
        
        int[] s_date = to_add.getDate();

        int lo = 0;
        int hi = batches.size() - 1;

        if(batches.size() == 0){
            batches.add(to_add);
            return;
        }

        if(is_later(batches.get(lo).getDate(), s_date)){
            batches.add(lo, to_add);
            return;
        }

        if(is_later(s_date, batches.get(hi).getDate())){
            batches.add(to_add);
            return;
        }
        while(hi != lo){ 
            int mid = (hi + lo) / 2; 
            if(is_later(s_date, batches.get(mid).getDate())){
                lo = mid + 1;
                if(is_later(batches.get(lo).getDate(), s_date)){
                    batches.add(lo, to_add);
                    return;
                }
            }
            else{
                hi = mid - 1;
            }
            
        }
        if(lo + 1 >= batches.size()){
            batches.add(to_add);
            return;
        }

        batches.add(lo + 1, to_add);
        return;
        
    }

   //must ensure that to_find is a valid date
   //tested
   public static int findbyDate(ArrayList<BatchT> batches, int l, int r, int[] to_find){
        if (r >= l) {
            int mid = l + (r - l) / 2;
 
            if (java.util.Arrays.equals(batches.get(mid).getDate(), to_find))
                return mid;
 
            if (is_later(batches.get(mid).getDate(), to_find))
                return findbyDate(batches, l, mid - 1, to_find);
 
            return findbyDate(batches, mid + 1, r, to_find);
        }
        return -1;
    }

    //tested
    //Assumes all date inputs are valid formats
    private static boolean is_later(int[] l_date, int[] e_date){ //this could be put in constants / services module for use in is_expired with EXP range passed as a length three int[]
       // if(l_date.length != 3 || l_date.length != e_date.length){
      //      throw new InputMismatchException("Invalid date format");
       // } is always checked for earlier

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
        batches.add(new BatchT(ProductEnum.GREEN, new int[] {2021, 4, 20}, 4, 4));
        batches.add(new BatchT(ProductEnum.GREEN, new int[] {2007, 5, 16}, 4, 4));
        batches.add(new BatchT(ProductEnum.GREEN, new int[] {2003, 4, 20}, 4, 4));
        batches.add(new BatchT(ProductEnum.GREEN, new int[] {2001, 3, 30}, 4, 4));
        batches.add(new BatchT(ProductEnum.GREEN, new int[] {2021, 3, 30}, 4, 4));

        BatchT to_add = new BatchT(ProductEnum.GREEN, new int[] {2021, 4, 18}, 4, 4);
        quickSort(batches);

        for(BatchT b : batches){
            print(b);
        }
        System.out.println();
        System.out.println(findbyDate(batches, 0, batches.size() - 1, new int[] {2001, 3, 30}));
    }

    

    public static void print(BatchT to_print){
        int[] date = to_print.getDate();
        for(int i : date){
            System.out.print(Integer.toString(i) + "/");
        }
        System.out.print(", ");
    }

    public static void quickSort(ArrayList<BatchT> batches){
        quickSortRec(batches, 0, batches.size() - 1);
    }

    private static void quickSortRec(ArrayList<BatchT> batches, int begin, int end) { //works!
        if (begin < end) {
            int partitionIndex = partition(batches, begin, end);
    
            quickSortRec(batches, begin, partitionIndex-1);
            quickSortRec(batches, partitionIndex+1, end);
        }
    }

    private static int partition(ArrayList<BatchT> batches, int begin, int end) {
        int[] pivot = batches.get(end).getDate();
        int i = (begin-1);
    
        for (int j = begin; j < end; j++) {
            if (is_later(pivot, batches.get(j).getDate())) {
                i++;
    
                BatchT swapTemp = batches.get(i);
                batches.set(i, batches.get(j));
                batches.set(j, swapTemp);
            }
        }
    
        BatchT swapTemp = batches.get(i+1);
        batches.set(i+1, batches.get(end));
        batches.set(end, swapTemp);
    
        return i+1;
    }

}
