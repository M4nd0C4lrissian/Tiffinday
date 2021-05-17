import java.util.ArrayList;

public class MultiBatchT { //MultiBatch deemed unnecesary - it is only for jars, which doesn't require its own object,
// they can just ship however many jars from wherever they want as needed
// something like this could eventually be used so that all leftover jars could easily be seen in one place
//also implies that batch interface is not necessary 
    private ArrayList<ShipmentT> where_to; 
    private ArrayList<Integer> jars;
    
    public MultiBatchT(ProductEnum p, ArrayList<BatchT> batches){

    }

}
