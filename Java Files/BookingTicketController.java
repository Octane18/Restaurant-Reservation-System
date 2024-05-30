import java.util.HashMap;

public class BookingTicketController {
    private static BookingTicketController instance = new BookingTicketController();
    private HashMap<Day, Integer> Tickets;
    private BookingTicketController() {
        Tickets = new HashMap<>();
    }
    public static BookingTicketController getInstance() {
        return instance;
    }
    public int getTicket(Day day){
        if(Tickets.containsKey(day)){
            int temp = Tickets.get(day);
           Tickets.put(day, Tickets.get(day)+1);
           return temp+1;
        }
        else {
            Tickets.put(day, 1);
            return 1;
        }
    }
    public void removeTicket(Day day){
        if(Tickets.containsKey(day)){
            int temp = Tickets.get(day);
            Tickets.put(day, temp-1);
        }
    }
}
