public class CmdRequest extends RecordedCommand{

    private Reservation reservation;

    public void execute(String[] cmdParts) throws ExInsufficientArguments, ExAlreadyExists, ExDatePassed{
        if(cmdParts.length<5) {
            throw new ExInsufficientArguments();
        }
        else {
            BookingOffice bo = BookingOffice.getInstance();
            Reservation r = new Reservation(cmdParts[1], cmdParts[2], Integer.parseInt(cmdParts[3]), cmdParts[4]);
            if(bo.exists(r)) {
                throw new ExAlreadyExists();
            }
            else {
                bo.addReservation(r);
                addUndoCommand(this);
                clearRedoList();
                System.out.print("Done.");
                r.confirmRes();
                reservation = r;
            }
        }

    }

    @Override
    public void undoMe() {
        BookingOffice bo = BookingOffice.getInstance();
        bo.removeReservations(reservation);
        reservation.Ticketremove();
        addRedoCommand(this);
    }

    @Override
    public void redoMe() {
        BookingOffice bo = BookingOffice.getInstance();
        bo.addReservation(reservation);
        reservation.Ticketreacquire();
        addUndoCommand(this);
    }

}
