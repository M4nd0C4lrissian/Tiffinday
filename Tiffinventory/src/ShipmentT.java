import java.util.ArrayList;
//might just make all this shit protected so I don't have to add shitty access programs
//but I probably should anyways for good practice

public class ShipmentT {

    private ArrayList<ShipInfoT> shipments = new ArrayList<ShipInfoT>();

    public ShipmentT(){}

    public void add_ship(int c, int j, String place){
        shipments.add(new ShipInfoT(c, j, place));
    }

    private class ShipInfoT{

        private int[] date_sent = new int[3];
        private String place_sent;
        private int invoice_num; //for now will be inputted, later just iterated from base num
        private int c_sent;
        private int j_sent;
        
        ShipInfoT(int c, int j, String place){
            String[] c_date = java.time.LocalDate.now().toString().split("-");
            for(int i = 0 ; i < 3 ; i++){
                this.date_sent[i] = Integer.parseInt(c_date[i]);
            }
            this.c_sent = c;
            this.j_sent = j;
            this.invoice_num = 0; //for now
        }
    }
}
