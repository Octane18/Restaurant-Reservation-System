public class ExTablesAlreadyAssigned extends ExControl{
    public ExTablesAlreadyAssigned() {
        super("Table(s) already assigned for this booking!");
    }
    public ExTablesAlreadyAssigned(String message) {
        super(message);
    }
}