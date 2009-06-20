package at.fakeroot.sepm.shared.server;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.Statement;

import junit.framework.TestCase;

public class GeoObjectManagerTest extends TestCase {
	
	GeoObjectManager geoObjManager;
	String[] tags;
	Property[] prop;
	DBGeoObject inObj;
	
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
				obj_id = geoObjManager.getObjectId(svcId, "test_karlskirche");
			}
			catch (Exception e) {
				fail("object 'test_karlskirche' not found! "+e.getMessage());
			}
			
			try {
				DBGeoObject geoObject = geoObjManager.getObjectbyId(obj_id);
			}
			catch (Exception e) {
				fail("couldn't resolve object id "+obj_id+": "+e.getMessage());
			}
			
			try{
				geoObjManager.insert(inObj);
				long outObjId= geoObjManager.getObjectId(0, "1706188");
				DBGeoObject outObj = geoObjManager.getObjectbyId(outObjId);
				
				assertEquals(inObj.getTitle(), outObj.getTitle());
				assertEquals(inObj.getLink(), outObj.getLink());
				assertEquals(inObj.getSvc_id(), outObj.getSvc_id());
				assertEquals(inObj.getUid(), outObj.getUid());
				assertEquals(inObj.getXPos(), outObj.getXPos());
				assertEquals(inObj.getYPos(), outObj.getYPos());
				assertArrayEquals(inObj.getProperties(), outObj.getProperties());
				assertArrayEquals(inObj.getTags(), outObj.getTags());
			}catch(Exception e){
				fail(e.getMessage());
			}
		}
	}

	@Override
	protected void setUp() throws Exception {
		geoObjManager = GeoObjectManager.getInstance();
		tags= new String[]{"nacht", "see", "baum"};
		prop = new Property[]{new Property("owner", "lacitot")};
		int svc_id= ServiceManager.getInstance().select("panoramio.com").getSvc_id();
		inObj = new DBGeoObject("Night", 21.440957, 48.427236, svc_id, "1706188", "http://www.panoramio.com/photo/1706188", java.sql.Timestamp.valueOf("2009-10-10 09:01:10"),prop, tags) ;
		
	}

}

