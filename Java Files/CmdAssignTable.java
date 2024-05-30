import java.util.ArrayList;

public class CmdAssignTable extends RecordedCommand{
    private ArrayList<Table> prevallocated;
    private Reservation prev;
    private Day dayAlloc;
    private String[] prevtables;

    @Override
    public void execute(String[] cmdParts) throws ExControl, CloneNotSupportedException, NumberFormatException{
        if(cmdParts.length<4) {
            throw new ExInsufficientArguments();
        }
        SystemDate sysdate = SystemDate.getInstance();
        Day d = new Day(cmdParts[1]);
        if(d.compareTo(sysdate)==-1) {
            throw new ExDatePassed();
        }
        BookingOffice bo = BookingOffice.getInstance();
        TableController tc = TableController.getInstance();
        Reservation r = bo.getReservationTicket(Integer.parseInt(cmdParts[2]), d);
        ArrayList<Table> allocated = new ArrayList<>();
        String[] tables = new String[cmdParts.length-3];
        for(int i = 3; i < cmdParts.length; i++){
            tables[i-3] = cmdParts[i];
        }
        allocated = tc.allocate(tables, new Day(cmdParts[1]), r);
        try{
            RState newState = new RStateTableAllocated(allocated);
            r.allocateTable(newState);
            prevallocated = allocated;
            prev = r;
            prevtables = tables;
            dayAlloc = new Day(cmdParts[1]);
            clearRedoList();
            addUndoCommand(this);
            System.out.println("Done.");
        }
        catch(ExTablesAlreadyAssigned e) {
            tc.deallocate(allocated, new Day(cmdParts[1]), r);
            throw new ExTablesAlreadyAssigned();
        }
    }
    @Override
    public void undoMe() {
        prev.revert(new RStatePending());
        TableController tc = TableController.getInstance();
        tc.deallocate(prevallocated, dayAlloc, prev);
        addRedoCommand(this);
    }

    @Override
    public void redoMe() throws CloneNotSupportedException, ExTablesAlreadyAssigned, ExNotEnoughSeats, ExReservedTable, ExTableNotFound {
        TableController tc = TableController.getInstance();
        tc.allocate(prevtables, dayAlloc, prev);
        prev.allocateTable(new RStateTableAllocated(prevallocated));
        addUndoCommand(this);
    }
}
