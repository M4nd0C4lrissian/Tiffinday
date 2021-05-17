public class CaseT {
    private ProductEnum prod;
    private final int num = 12;
    CaseT(ProductEnum p){
        this.prod = p;
    }

    public ProductEnum getProd(){
        return prod;
    }

    public int getVal(){
        return num;
    } 
}
