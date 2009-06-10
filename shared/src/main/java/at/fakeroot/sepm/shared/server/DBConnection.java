/**
 * 
 */
package at.fakeroot.sepm.shared.server;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.ConnectionPoolDataSource;

import org.postgresql.ds.PGConnectionPoolDataSource;

/**
 * Provides a Database Connection
 * This class uses a static connection pool that is shared by all DBConnection objects.
 *
 * @author Manuel Reithuber
 * 
 */
public class DBConnection {
	// shared connection pool
	private static ConnectionPoolDataSource dataSource;
	private static boolean isTesting = false;
	private Connection dbConn;
	
	public DBConnection() throws SQLException, IOException {
		if (dataSource == null)	{
			Properties prop = new Properties();

			PGConnectionPoolDataSource ds = new PGConnectionPoolDataSource();
			
			InputStream propStream = getClass().getResourceAsStream("/WEB-INF/jdbc.properties");
			if (propStream == null) {
				throw new IOException("Error: Couldn't open property file 'jdbc.properties'");
			}
			prop.load(propStream);

			ds.setServerName(prop.getProperty("host"));
			ds.setDatabaseName(prop.getProperty("db"));
			ds.setUser(prop.getProperty("user"));
			ds.setPassword(prop.getProperty("pwd"));
			dataSource = ds;
			isTesting = true;
		}
	}
	
	public void disconnect() throws SQLException {
		if (dbConn != null) {
			if (isTesting) {
				// rollback transaction
				Statement s = dbConn.createStatement();
				s.execute("ROLLBACK");
			}
			dbConn.close();
		}
	}
	
	public Connection getConnection() throws SQLException {
		if (dbConn == null) {
			dbConn = dataSource.getPooledConnection().getConnection();
			if (isTesting) {
				// begin transaction
				Statement s = dbConn.createStatement();
				s.execute("BEGIN");
			}
		}
		return dbConn;
	}
	
	public PreparedStatement prepareStatement(String stmt) throws SQLException {
		return getConnection().prepareStatement(stmt, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}
	
	public Statement createStatement() throws SQLException {
		return getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}
}
