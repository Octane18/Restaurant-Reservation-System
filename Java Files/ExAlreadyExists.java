public class ExAlreadyExists extends ExControl {
    public ExAlreadyExists() {
        super("Booking by the same person for the dining date already exists!");
    }
    public ExAlreadyExists(String message) {
        super(message);
    }
}
