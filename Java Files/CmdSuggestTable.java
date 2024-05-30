import java.util.ArrayList;

public class CmdSuggestTable implements Command{
    @Override
    public void execute(String[] cmdparts) throws ExControl{
        if(cmdparts.length < 3){
            throw new ExInsufficientArguments();
        }
        SystemDate sysdate = SystemDate.getInstance();
        Day d = new Day(cmdparts[1]);
        BookingOffice bo = BookingOffice.getInstance();
        TableController tc = TableController.getInstance();
        Reservation r = bo.getReservationTicket(Integer.parseInt(cmdparts[2]), d);
        if(!r.getStatus().equals("Pending")) {
            throw new ExTablesAlreadyAssigned();
        }
        if(r.compare(sysdate)==-1) {
            throw new ExDatePassed();
        }
        ArrayList<String> suggested = tc.suggest(r.getTotalPersons(), d);
        System.out.printf("Suggestion for %d persons:", r.getTotalPersons());
        if(suggested==null) {
            System.out.println(" Not enough tables");
            return;
        }
        for(String suggestedTable : suggested){
            System.out.printf(" %s", suggestedTable);
        }
    }
}
