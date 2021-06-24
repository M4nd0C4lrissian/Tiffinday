public class Driver {
    public static void main(String[] args){
        ActiveBatchD.setup();
        BatchQueue.setup();
        Trigger.setup();
      
        ///*
        BatchQueue.new_batch(ProductEnum.YELLOW, new int[] {2017, 10, 20}, 6, 2);
        BatchQueue.new_batch(ProductEnum.YELLOW, new int[] {2019, 04, 20}, 6, 2);
        BatchQueue.new_batch(ProductEnum.YELLOW, new int[] {2019, 04, 20}, 6, 2);
        BatchQueue.new_batch(ProductEnum.YELLOW, new int[] {2019, 11, 30}, 2, 3);
        BatchQueue.new_batch(ProductEnum.YELLOW, new int[] {2019, 03, 17}, 2, 5);
        BatchQueue.new_batch(ProductEnum.YELLOW, new int[] {2019, 12, 9}, 0, 1);
        //*/

        //BatchQueue.new_batch(ProductEnum.YELLOW, new int[] {2021, 04, 20}, 6, 2);

        System.out.println(Trigger.total_c[0]);
        System.out.println(Trigger.total_j[0]);
        
        BatchQueue.disp_batches(ProductEnum.YELLOW);

        System.out.println();


       BatchQueue.ship(ProductEnum.YELLOW, "my house", 10, 3); 
        BatchQueue.disp_batches(ProductEnum.YELLOW);

        System.out.println(Trigger.total_c[0]);
        System.out.println(Trigger.total_j[0]);
        
        
       
    }
}
