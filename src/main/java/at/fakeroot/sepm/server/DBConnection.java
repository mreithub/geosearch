/**
 * 
 */
package at.fakeroot.sepm.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.ConnectionPoolDataSource;

import org.postgresql.ds.PGConnectionPoolDataSource;

/**
 * @author Manuel Reithuber
 * 
 * @note This class uses pooled connections.
 * 
 */
public class DBConnection {
	// shared connection pool
	private static ConnectionPoolDataSource dataSource;
	private static boolean isTesting = false;
	private Connection dbConn;
	
	public DBConnection() throws SQLException {
		if (dataSource == null)	{
			// Database initialization @TODO dynamically get DB config
			PGConnectionPoolDataSource ds = new PGConnectionPoolDataSource();
			ds.setServerName("fakeroot.at");
			ds.setDatabaseName("geosearch_test");
			ds.setUser("geosearch_test");
			ds.setPassword("Waerie4Petei4eey");
			dataSource = ds;
			isTesting = true;
		}
	}
	
	public void disconnect() throws SQLException {
		if (dbConn != null) {
			if (isTesting) {
				// rollback transaction
				dbConn.setAutoCommit(true);
		//		Statement s = dbConn.createStatement();
		//		s.execute("ROLLBACK");
			}
			dbConn.close();
		}
	}
	
	public Connection getConnection() throws SQLException {
		if (dbConn == null) {
			dbConn = dataSource.getPooledConnection().getConnection();
			if (isTesting) {
				// begin transaction
				dbConn.setAutoCommit(false);
				//Statement s = dbConn.createStatement();
				//s.execute("BEGIN");
			}
		}
		return dbConn;
	}
	
	public PreparedStatement prepareStatement(String stmt) throws SQLException {
		return getConnection().prepareStatement(stmt);
	}
	
	public Statement createStatement() throws SQLException {
		return getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}
}
