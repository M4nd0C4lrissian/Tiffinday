public enum ProductEnum{
    YELLOW(0), 
    GREEN(1), 
    ORANGE(2), 
    PINK(3);    

    private int index;

    public int get_ind(){
        return index;
    }

    private ProductEnum(int i){
        this.index = i;
    }
}