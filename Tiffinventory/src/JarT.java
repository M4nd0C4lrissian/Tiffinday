public class JarT implements Sellable{
    private ProductEnum prod;
    private final int num = 1;
    JarT(ProductEnum p){
        this.prod = p;
    }

    public ProductEnum getProd(){
        return prod;
    }

    public int getVal(){
        return num;
    }
}
