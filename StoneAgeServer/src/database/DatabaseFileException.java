package database;


public class DatabaseFileException extends Exception {

    public DatabaseFileException() {
        super("DatabaseFile not found!");
    }

    public DatabaseFileException(String msg) {
        super(msg);
    }

}
