package at.fakeroot.sepm.server;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.Statement;

import at.fakeroot.sepm.shared.server.DBGeoObject;
import at.fakeroot.sepm.shared.server.Property;

import junit.framework.TestCase;

public class GeoObjectManagerTest extends TestCase {
	
	GeoObjectManager geoObjManager;
	String[] tags;
	Property[] prop;
	DBGeoObject inObj;
	int panoramio_id;
	
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
		
		// don't mess around with the productive database 
		if (dbConn != null && dbConn.isTesting()) {
			try {
				obj_id = geoObjManager.getObjectId(svcId, "test_karlskirche");
				DBGeoObject outObj = geoObjManager.getObjectbyId(obj_id);
				
				assertEquals("Karlskirche", outObj.getTitle());
				assertEquals("http://de.wikipedia.org/wiki/Wiener_Karlskirche", outObj.getLink());
				assertEquals(16.371422, outObj.getXPos());
				assertEquals(48.198247, outObj.getYPos());
			}
			catch (Exception e) {
				fail("object 'test_karlskirche' not found! "+e.getMessage());
			}
			
			try {
				DBGeoObject geoObject = geoObjManager.getObjectbyId(obj_id);
				assertEquals(obj_id, geoObject.getId());
			}
			catch (Exception e) {
				e.printStackTrace();
				fail("couldn't resolve object id "+obj_id+": "+e.getMessage());
			}
			
			try{
				// we can't try to get the data out again because GeoObjectManager uses two
				// different DBConnections to read/write data to the database
				geoObjManager.insert(inObj);
				
			}catch (RemoteException e) {
				e.printStackTrace();
				fail("RemoteException: "+e.getMessage());
			}
		}
	}

	@Override
	protected void setUp() throws Exception {
		geoObjManager = GeoObjectManager.getInstance();
		tags= new String[]{"nacht", "see", "baum"};
		prop = new Property[]{new Property("owner", "lacitot")};
		panoramio_id= ServiceManager.getInstance().select("panoramio.com").getSvc_id();
		inObj = new DBGeoObject("Night", 21.440957, 48.427236, panoramio_id, "test_panoramio", "http://www.panoramio.com/photo/1706188", java.sql.Timestamp.valueOf("2009-10-10 09:01:10"),prop, tags) ;
		
	}

}

