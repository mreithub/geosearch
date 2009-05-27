/**
 * 
 */
package at.fakeroot.sepm.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import junit.framework.TestCase;

/**
 * @author Manuel Reithuber
 *
 */
public class DBConnectionTest extends TestCase {
	public String getModulName(){
		return "at.fakeroot.sepm.server.DBConnection";
	}

	public void testSimple(){
		DBConnection db;

		try {
			db = new DBConnection();
			Statement s = db.createStatement();
			ResultSet r = s.executeQuery("SELECT name FROM service");
			while (r.next()) {
				System.out.println("Service: "+r.getString(1));
			}
		}
		catch (SQLException e) {
			assertTrue("SQL Exception: "+e.getMessage(), false);
		}
	}
}
