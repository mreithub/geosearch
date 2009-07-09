package at.fakeroot.sepm.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public interface IDBConnection {

	/**
	 * release the DBConnection
	 * 
	 * This function releases the internal PooledConnection.
	 * @throws SQLException if the transaction rollback (if in testing mode)
	 *	or the db closing fails 
	 */
	public abstract void disconnect() throws SQLException;

	/**
	 * returns the internal Connection object
	 * (requests one from the connection pool if there is none yet)
	 * @return internal Connection object
	 * @throws SQLException if the pooled connection couldn't be requested
	 */
	public abstract Connection getConnection() throws SQLException;

	/**
	 * returns a PreparedStatement with scrollable ResultSets enabled
	 * @param stmt SQL statement to prepare
	 * @return PreparedStatement
	 * @throws SQLException if the database call fails
	 */
	public abstract PreparedStatement prepareStatement(String stmt)
			throws SQLException;

	/**
	 * creates and returns a plain Statement with scrollable ResultSets enabled
	 * @return Statement
	 * @throws SQLException if the database call fails
	 */
	public abstract Statement createStatement() throws SQLException;

	/**
	 * thin wrapper around Connection.setAutoCommit() (use this with commit() and rollback()) 
	 * @param value determines if auto commit will be enabled
	 * @throws SQLException if the database call fails
	 */
	public abstract void setAutoCommit(boolean value) throws SQLException;

	/**
	 * commit current transaction (thin wrapper around Connection.commit())
	 * @throws SQLException if the database call fails
	 */
	public abstract void commit() throws SQLException;

	/**
	 * cancels the current transaction (thin wrapper around Connection.rollback())
	 * @throws SQLException if the database call fails
	 */
	public abstract void rollback() throws SQLException;

	/**
	 * returns true if DBConnection is set to testing mode.
	 * 
	 * Testing mode can be enabled/disabled in jdbc.properties.
	 * If enabled, all queries are put into a transaction that is rolled back
	 * when the connection is released (using disconnect()). 
	 * 
	 * @return true: DBConnection is in testing mode, false otherwise
	 */
	public abstract boolean isTesting();

}