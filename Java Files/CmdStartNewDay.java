public class CmdStartNewDay extends RecordedCommand {
    SystemDate prev;
    public void execute(String[] cmdParts) throws ExInsufficientArguments{
        if(cmdParts.length < 2){
            throw new ExInsufficientArguments();
        }
        SystemDate instance = SystemDate.getInstance();
        prev = instance;
        instance.startNewDay(cmdParts[1]);
        SystemDate temp = SystemDate.getInstance();
        addUndoCommand(this);
        clearRedoList();
        System.out.println("Done.");
    }

    @Override
    public void undoMe() {
        SystemDate instance = SystemDate.getInstance();
        SystemDate temp = instance;
        SystemDate.setIn(prev);
        prev = temp;
        addRedoCommand(this);
    }

    @Override
    public void redoMe() {
        SystemDate instance = SystemDate.getInstance();
        SystemDate temp = instance;
        instance = prev;
        prev = temp;
        addUndoCommand(this);
    }
}
