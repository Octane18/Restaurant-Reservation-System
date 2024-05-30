
import java.util.ArrayList;

public class CmdCancelBooking extends RecordedCommand{
    private ArrayList<Table> prevallocated = null;
    private String[] prevAllocatedtables;
    private Reservation r;
    private Day d;

    @Override
    public void execute(String[] cmdParts) throws ExControl, CloneNotSupportedException, NumberFormatException{
        if(cmdParts.length<3) {
            throw new ExInsufficientArguments();
        }
        TableController tc = TableController.getInstance();
        BookingOffice bo = BookingOffice.getInstance();
        SystemDate sysdate = SystemDate.getInstance();
        Reservation reservation = bo.getReservationTicket(Integer.parseInt(cmdParts[2]), new Day(cmdParts[1]));
        if(reservation.compare(sysdate)==-1) {
            throw new ExDatePassed();
        }
       if(!reservation.getStatus().toString().equals("Pending")) {
           ArrayList<Table> allocated = tc.getAllocated(new Day(cmdParts[1]), Integer.parseInt(cmdParts[2]));
           tc.deallocate(allocated, new Day(cmdParts[1]), reservation);
           prevallocated = allocated;
           prevAllocatedtables = new String[prevallocated.size()];
           for(int i=0; i<prevallocated.size(); i++) {
               prevAllocatedtables[i] = prevallocated.get(i).toString();
           }
       }
        bo.removeReservations(reservation);
        r = reservation;
        d = new Day(cmdParts[1]);

        clearRedoList();
        addUndoCommand(this);
        System.out.println("Done.");

    }
    @Override
    public void undoMe() throws ExControl, CloneNotSupportedException {
        TableController tc = TableController.getInstance();
        BookingOffice bo = BookingOffice.getInstance();
        bo.addReservation(r);
        if(!(prevallocated ==null)) {
            tc.allocate(prevAllocatedtables, d, r);
        }
        addRedoCommand(this);
    }

    @Override
    public void redoMe() throws CloneNotSupportedException, ExControl {
        BookingOffice bo = BookingOffice.getInstance();
        TableController tc = TableController.getInstance();
        bo.removeReservations(r);
        if(!(prevallocated ==null)) {
            tc.deallocate(prevallocated, d, r);
        }
        addUndoCommand(this);

    }
}
