public interface Command {
    void execute(String[] cmdparts) throws ExControl, CloneNotSupportedException;
}
