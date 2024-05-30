import java.util.ArrayList;

public class RStateTableAllocated implements RState{
    private ArrayList<Table> tableList;
    public RStateTableAllocated(ArrayList<Table> tableList) {
        this.tableList = tableList;
    }
    @Override
    public String toString() {
        String retString = "Table assigned:";
        for (Table table : tableList) {
            retString = String.format("%s %s", retString, table);
        }
        return retString;
    }

}

