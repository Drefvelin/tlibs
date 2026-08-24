package me.Plugins.TLibs.database;

public class SqliteDatabaseException extends RuntimeException {
    public SqliteDatabaseException(String message) {
        super(message);
    }

    public SqliteDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
