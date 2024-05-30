import java.util.ArrayList;
public abstract class RecordedCommand implements Command{
    public abstract void undoMe() throws ExControl, CloneNotSupportedException;
    public abstract void redoMe() throws CloneNotSupportedException, ExControl;
    public void execute(String[] cmdParts) throws ExControl, CloneNotSupportedException {}
    private static ArrayList<RecordedCommand> undoList = new ArrayList<>();
    private static ArrayList<RecordedCommand> redoList = new ArrayList<>();
    protected static void addUndoCommand(RecordedCommand cmd){undoList.add(cmd);}
    protected static void addRedoCommand(RecordedCommand cmd){redoList.add(cmd);}
    protected static void clearRedoList() {redoList.clear();}
    public static void undoOneCommand() throws ExControl, CloneNotSupportedException {
        if(isUndoEmpty()) {
            System.out.println("Nothing to undo.");
        }
        else {
            undoList.remove(undoList.size()-1).undoMe();
        }
    }
    public static void redoOneCommand() throws CloneNotSupportedException, ExControl {
        if(isRedoEmpty()){
            System.out.println("Nothing to redo.");
        }
        else {
            redoList.remove(redoList.size()-1).redoMe();
        }
    }
    public static boolean isUndoEmpty() {
        return undoList.isEmpty();
    }
    public static boolean isRedoEmpty() {
        return redoList.isEmpty();
    }

}
