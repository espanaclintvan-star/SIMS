	import java.sql.Connection;
	import java.sql.SQLException;
	import java.util.Properties;
	import org.postgresql.Driver;

public class database {
	
	    // Database credentials
	    private static final String URL = "jdbc:postgresql://localhost:5432/SIMS"; // Replace with your DB name
	    private static final String USER = "postgres"; // Your PostgreSQL username
	    private static final String PASSWORD = "gwapoko123"; // Your PostgreSQL password

	    public static Connection getConnection() throws SQLException {
	        Properties credentials = new Properties();
	        credentials.setProperty("user", USER);
	        credentials.setProperty("password", PASSWORD);

	        Connection connection = new Driver().connect(URL, credentials);
	        if (connection == null) {
	            throw new SQLException("PostgreSQL JDBC driver did not accept the database URL: " + URL);
	        }
	        return connection;
	    }

}
