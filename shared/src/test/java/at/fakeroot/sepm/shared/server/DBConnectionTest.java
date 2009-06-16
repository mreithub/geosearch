/**
 * 
 */
package at.fakeroot.sepm.shared.server;

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
		DBConnection db = null;

		try {
			db = new DBConnection();
		}
		catch (Exception e) {
			System.err.println("Database connection failed:");
			e.printStackTrace(System.err);
			System.exit (0);
		}

		// only do this if we are on the testing database
		if (db.isTesting()) {
			try {
				Statement s = db.createStatement();
				s.executeUpdate("INSERT INTO serviceType (name, thumbnail) VALUES ('testtype', '/img/test1.png')");
				s.executeUpdate("INSERT INTO serviceType (name, thumbnail) VALUES ('testtype2', '/img/test2.png')");

				ResultSet r = s.executeQuery("SELECT name FROM serviceType");
				// check if rowcount = 5
				r.last();
				int rowcount = r.getRow();

				// make sure that the DB transaction is rolled back before we run an assert 
				db.disconnect();
			
				assertEquals(5, rowcount);
			}
			catch (Exception e) {
				e.printStackTrace();
				fail("Exception: "+e.getMessage());
				
			}
			finally {
				try {
					if (db != null) db.disconnect();
				}
				catch (SQLException e) {
					// doNothing
				}
			}
		}
	}
}
