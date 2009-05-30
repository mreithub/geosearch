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
			s.executeUpdate("INSERT INTO serviceType (name, thumbnail) VALUES ('picture', '/img/pic.png')");
			s.executeUpdate("INSERT INTO serviceType (name, thumbnail) VALUES ('lexicon', '/img/lexicon.png')");
			
			ResultSet r = s.executeQuery("SELECT name FROM serviceType");
			// check if rowcount = 2
			r.last();
			assertEquals(2, r.getRow());
			db.disconnect();
		}
		catch (SQLException e) {
			assertTrue("SQL Exception: "+e.getMessage(), false);
		}
	}
}
