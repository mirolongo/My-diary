package mydiary;

import org.sqlite.SQLiteDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    private static final Path DB_PATH = Path.of("diary.db");
    private static final Path DB_SCRIPT_PATH = Path.of("create.sql");
    private static SQLiteDataSource db;
    public static void main(String[] args) throws SQLException, IOException {
        db = new SQLiteDataSource();
        db.setUrl("jdbc:sqlite:" + DB_PATH);
        if (!Files.exists(DB_PATH)) {
            createDatabase();
        }

    }

    private static void createDatabase() throws SQLException, IOException {
        try {
            String sql = Files.readString(DB_SCRIPT_PATH);
            String[] commands = sql.split(System.lineSeparator() + System.lineSeparator());
            try (Connection c = db.getConnection()) {
                for (String command : commands) {
                    Statement s = c.createStatement();
                    s.executeUpdate(command);
                }
            }
        } catch (SQLException e) {
            if (Files.exists(DB_PATH)) {
                Files.delete(DB_PATH);
            }
            throw e;
        }
    }
}
