public class Trigger { //this class will need to entirely interface with the database
    
    //Note for implementation -- user should normally only use BatchQueue to add Batches and ship from them, and thus Trigger and its associated totals will only have
    //to be interfaced with in BatchQueue and not BatchT / ShipmentT (other than their override methods). This means some extra care will have to go into ensuring that other means of messing 
    //with values are protected from the user.

    protected static int[] total_j = new int[ProductEnum.values().length]; //this needs to be constantly written to database 
    protected static int[] total_c = new int[ProductEnum.values().length];
    protected static int[] triggers = new int[ProductEnum.values().length]; //trigger is only for cases - should also be written in database 
    
    public static void decr_prod(ProductEnum p, int c, int j){ //negative inputs increment
        int index = p.get_ind();
        total_c[index] -= c;
        total_j[index] -= j;
        triggered(p);
    }


    public static void triggered(ProductEnum p){ //consider setup - and if and when triggers will be checked upon initial inventory addition
        int index = p.get_ind();
        if(total_c[index] < triggers[index]){
            System.out.println("Stock of " + p.name() + " cases is less than the desired amount as indicated by the trigger");
        }
    }

    public static void set_triggers(int[] trigs){
        if(trigs.length != triggers.length){
            System.out.println("Invalid trigger input"); //these checks are probably both unnecessary but here for now to represent assumptions later
        }

        for(int i : trigs){
            if(i < 0){
                System.out.println("Invalid trigger values (use positive numbers)");  //they / we can input 0 if they do not want a trigger for that specific product
                return;
            }
        }

        for(int j = 0 ; j < triggers.length ; j++){
            triggers[j] = trigs[j];
        }



    }
}
