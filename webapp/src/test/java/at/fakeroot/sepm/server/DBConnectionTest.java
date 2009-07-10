/**
 * 
 */
package at.fakeroot.sepm.server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Manuel Reithuber
 *
 */
public class DBConnectionTest  extends TestCase {
	IDBConnection db = null;

	@Test
	public void testSimple(){


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
			
				assertEquals(3, rowcount);
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

	@Before
	public void setUp() throws IOException {
		db = new DBConnection();
		
	}
	
	@After
	public void tearDown() {
		
	}
}
