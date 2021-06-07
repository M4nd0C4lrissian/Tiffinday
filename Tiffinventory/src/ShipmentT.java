import java.util.ArrayList;

//extra notes can be written in place info
//make possible to remove / alter shipments later
public class ShipmentT {

    private ArrayList<ShipInfoT> shipments = new ArrayList<ShipInfoT>(); //this isn't necessary -- just put everything straight into table

    public ShipmentT(){} 

    public void add_ship(int c, int j, String place){
        shipments.add(new ShipInfoT(c, j, place));
    }

    public ArrayList<ShipInfoT> getShip(){
        return shipments;
    }

    public String where_sent(int index){
        is_outBounds(index);
        return shipments.get(index).place_sent;
    }

    public int[] getDate(int index){
        is_outBounds(index);
        return shipments.get(index).date_sent;
    }

    public int getInvoice_num(int index){
        is_outBounds(index);
        return shipments.get(index).invoice_num;
    }

    public int[] getSent(int index){
        is_outBounds(index);
        ShipInfoT ship = shipments.get(index);
        return new int[] {ship.c_sent, ship.j_sent};
    } 

    private void is_outBounds(int index) throws IndexOutOfBoundsException{
        if(shipments.size() < index + 1){
            throw new IndexOutOfBoundsException();
        }
    }


    private class ShipInfoT{

        public int[] date_sent = new int[3];
        public String place_sent;
        public int invoice_num; //for now will be inputted, later just iterated from value in database
        public int c_sent;
        public int j_sent;
        
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
