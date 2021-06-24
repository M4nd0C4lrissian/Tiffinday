
//Assumption - setup() will be called before any other access routine
public class Trigger {
    
    //Note for implementation -- user should normally only use BatchQueue to add Batches and ship from them, and thus Trigger and its associated totals will only have
    //to be interfaced with in BatchQueue and not BatchT / ShipmentT (other than their override methods). This means some extra care will have to go into ensuring that other means of messing 
    //with values are protected from the user.

    protected static int[] total_c = new int[ProductEnum.values().length]; 
    protected static int[] total_j = new int[ProductEnum.values().length];
    protected static int[] triggers = new int[ProductEnum.values().length]; //only for cases
    
    public static void main(String[] args) {
        setup();
        set_triggers(new int[] {1,2,3,4});
        decr_prod(ProductEnum.YELLOW, 3, 0);
    }

    public static void setup(){
        TriggerD.setup();
        int[][] totals = TriggerD.get_totals();

        total_c = totals[0];                          
        total_j = totals[1];
        triggers = TriggerD.get_trigs();
    }



    public static void decr_prod(ProductEnum p, int c, int j){ //negative inputs increment
        int index = p.get_ind();
        total_c[index] -= c;
        total_j[index] -= j;
        TriggerD.set_totals(p, total_c[index], total_j[index]);

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
            return;
        }

        for(int i : trigs){
            if(i < 0){
                System.out.println("Invalid trigger values (use positive numbers)");  //they / we can input 0 if they do not want a trigger for that specific product
                return;
            }
        }
        
        TriggerD.set_trigs(trigs);
        triggers = trigs;
    }
}
