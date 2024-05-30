public class ExInsufficientArguments extends ExControl{
    public ExInsufficientArguments() {
        super("Insufficient command arguments!");
    }
    public ExInsufficientArguments(String message){
        super(message);
    }

}
