package at.fakeroot.sepm.shared.server;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.Statement;

import junit.framework.TestCase;

public class GeoObjectManagerTest extends TestCase {
	GeoObjectManager geoManager = GeoObjectManager.getInstance();
	
	public void testSimple() {
		DBConnection dbConn = null;
		Statement stmt;
		int svcId = -1;
		long obj_id = -1;
		
		// get svcId
		try {
			dbConn = new DBConnection();
			stmt = dbConn.createStatement();
			ResultSet r = stmt.executeQuery("SELECT svc_id FROM service WHERE name = 'de.wikipedia.org'");
			if (r.next()) {
				svcId = r.getInt(1);
			}
		}
		catch (Exception e) {
			fail("couldn't get svc id: "+e.getMessage());
		}
		
		// this data just lies in the test database 
		if (dbConn != null && dbConn.isTesting()) {
			try {
				obj_id = geoManager.getObjectId(svcId, "test_karlskirche");
			}
			catch (Exception e) {
				fail("object 'test_karlskirche' not found! "+e.getMessage());
			}
			
			try {
				DBGeoObject geoObject = geoManager.getObjectbyId(obj_id);
			}
			catch (Exception e) {
				fail("couldn't resolve object id "+obj_id+": "+e.getMessage());
			}
		}
	}
}
