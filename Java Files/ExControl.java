public class ExControl extends Exception{
    public ExControl(String message){
        super(message);
    }
    public ExControl(){
        super("Exception Control");
    }
}
