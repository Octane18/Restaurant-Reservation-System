public class CmdListTableAllocations implements Command {

    @Override
    public void execute(String[] cmdparts) throws ExInsufficientArguments{
        if(cmdparts.length<2) {
            throw new ExInsufficientArguments();
        }
        TableController tc = TableController.getInstance();
        BookingOffice bo = BookingOffice.getInstance();
        System.out.println("Allocated tables: ");
        tc.listAlloc(new Day(cmdparts[1]));
        System.out.println("Available tables: ");
        tc.listAvailable(new Day(cmdparts[1]));
        bo.listPending(new Day(cmdparts[1]));
    }
}
