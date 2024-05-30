import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Collections;

public class TableController {
    private ArrayList<Table> tables = new ArrayList<>();
    private int min_available_seats;
    private static TableController instance = new TableController();
    private HashMap<Day, ArrayList<Table>> dayTables = new HashMap<>();
    public TableController(){
        for(int i=0; i<9; i++){
            String code = String.format("T0%d", i+1);
            tables.add(new Table(code, 2));
        }
        tables.add(new Table("T10", 2));
        for(int i=0; i<6; i++) {
            String code = String.format("F0%d", i+1);
            tables.add(new Table(code, 4));
        }
        for(int i=0; i<3; i++){
            String code = String.format("H0%d", i+1);
            tables.add(new Table(code, 8));
        }
    }

    public static TableController getInstance() {
        return instance;
    }

    public ArrayList<String> suggest(int cap, Day d) {
        for (Table table : tables) {
            table.resetSuggestStatus();
        }
        ArrayList<String> suggestions = new ArrayList<>();
        boolean available8=true, available4=true, available2= true;
        while(cap>0 && (available8 || available4 || available2)) {
            if(available8 && (cap>=8 || (cap>4 && cap<8))) {
                boolean found = false;
                for(int i=16; i<tables.size(); i++) {
                    if(!tables.get(i).checkDate(d) && !tables.get(i).checksuggeststatus()) {
                        found = true;
                        cap-=tables.get(i).getCapacity();
                        suggestions.add(tables.get(i).toString());
                        tables.get(i).setSuggestStatus();
                        break;
                    }

                }
                if(!found) {
                    available8 = false;
                }
            }
            else if(available4 && (cap>=4|| (cap>2 && cap<4))) {
                boolean found = false;
                for(int i=10; i<16; i++) {
                    if(!tables.get(i).checkDate(d) && !tables.get(i).checksuggeststatus()) {
                        found = true;
                        cap-=tables.get(i).getCapacity();
                        suggestions.add(tables.get(i).toString());
                        tables.get(i).setSuggestStatus();
                        break;
                    }
                }
                if(!found) {
                    available4 = false;
                }
            }
            else if(available2 && cap>=2) {
                boolean found = false;
                for(int i=0; i<10; i++) {
                    if(!tables.get(i).checkDate(d) && !tables.get(i).checksuggeststatus()) {
                        found = true;
                        cap-=tables.get(i).getCapacity();
                        suggestions.add(tables.get(i).toString());
                        tables.get(i).setSuggestStatus();
                        break;

                    }
                }
                if(!found) {
                    available2 = false;
                }
            }
            else {
                for(Table t: tables) {
                    if (!t.checkDate(d) && !t.checksuggeststatus()) {
                        cap -= t.getCapacity();
                        suggestions.add(t.toString());
                        t.setSuggestStatus();
                        break;
                    }
                }
            }
        }
        if(cap>0) {
            return null;
        }
        return suggestions;


    }
    public ArrayList<Table> allocate(String[] tables, Day d, Reservation r) throws CloneNotSupportedException, ExReservedTable, ExNotEnoughSeats, ExTableNotFound {

        ArrayList<Table> ret = new ArrayList<>();
        for(int i=0; i<tables.length; i++){
            boolean tableFound = false;
            for(int j=0; j<this.tables.size(); j++){
                if(this.tables.get(j).equalsCode(tables[i])){
                    if(!this.tables.get(j).checkDate(d)) {
                        this.tables.get(j).addReservation(r);
                        ret.add((Table) this.tables.get(j).clone());
                        tableFound = true;
                    }
                    else {
                        deallocate(ret, d, r);
                        throw new ExReservedTable(String.format("Table %s is already reserved by another booking!", this.tables.get(j).toString()));
                    }

                }
            }
            if(!tableFound) {
                deallocate(ret, d, r);
                throw new ExTableNotFound(String.format("Table code %s not found!", tables[i]));
            }
        }

        ArrayList<Table> onDay = dayTables.get(d);
        if(onDay == null){
            onDay = (ArrayList<Table>) ret.clone();
            Collections.sort(onDay);
            dayTables.put(d, onDay);
        }
        else {
            onDay.addAll((Collection<? extends Table>) ret.clone());
            Collections.sort(onDay);

            dayTables.put(d, onDay);
        }
        int tablecap=0;
        for(int i=0; i<tables.length; i++){
            for(int j=0; j<this.tables.size(); j++) {
                if (this.tables.get(j).equalsCode(tables[i])) {
                    tablecap += this.tables.get(j).getCapacity();
                }
            }
        }
        if(tablecap<r.getTotalPersons()) {
            deallocate(ret, d, r);
            throw new ExNotEnoughSeats();
        }
        return ret;

    }

    public ArrayList<Table> getAllocated(Day d, int tnum) throws CloneNotSupportedException {
        ArrayList<Table> ret = new ArrayList<>(), onDay;
        onDay = (ArrayList<Table>) dayTables.get(d).clone();
        for(Table t : onDay) {
            if(tnum==t.getTicket(d)){
                ret.add(t);
            }
        }
        return ret;
    }
    public void listAlloc(Day d) {
        ArrayList<Table> onDay = dayTables.get(d);
        if(onDay == null){
            System.out.println("[None]");
            return;
        }
        for(int i=0; i<onDay.size(); i++){
          System.out.printf("%s (Ticket %d)\n", onDay.get(i).toString(), onDay.get(i).getTicket(d));
        }
    }

/*    private void checkMin(Day d){
        int i = 10000;
        for(Table t: tables) {
            if(!t.checkDate(d)) {
                if(t.getCapacity()<i) {
                    i = t.getCapacity();
                }
            }
        }
        min_available_seats = i;
    }*/


    public void listAvailable(Day d) {
        for(Table t: tables) {
            if(!t.checkDate(d)) {
                System.out.printf("%s ", t);
            }
        }
        System.out.println();
    }
    public void deallocate(ArrayList<Table> allocated, Day d, Reservation r){
        for (int i=0; i<allocated.size(); i++) {
            for(int j=0; j<tables.size(); j++) {
                if(tables.get(j).equals(allocated.get(i))) {
                    tables.get(j).removeReservation(r);
                }
            }
        }
        ArrayList<Table> temp = dayTables.get(d);
        if(!(temp ==null)) {
            temp.removeAll(allocated);
            if(temp.isEmpty()) {
                temp = null;
            }
            dayTables.put(d, temp);
        }

    }
}
