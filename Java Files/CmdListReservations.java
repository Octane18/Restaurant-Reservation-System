public class CmdListReservations implements Command {
    public void execute(String[] cmdParts) {
        BookingOffice bo = BookingOffice.getInstance();
        bo.listReservations();

    }
}
