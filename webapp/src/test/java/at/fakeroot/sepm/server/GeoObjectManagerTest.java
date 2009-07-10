package at.fakeroot.sepm.server;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*; 

import at.fakeroot.sepm.server.NotFoundException;
import at.fakeroot.sepm.shared.server.DBGeoObject;
import at.fakeroot.sepm.shared.server.Property;


public class GeoObjectManagerTest extends TestCase{
	
	IGeoObjectManager geoObjManager;
	String[] tags;
	Property[] prop;
	DBGeoObject inObj;
	int svcId;
	
	SortedSet<Long> createdObjects = new TreeSet<Long>();
	
	@Test
	public void insertObject_shouldSucceed() throws RemoteException, SQLException, NotFoundException {
		String uid = "testObject"+Calendar.getInstance().getTimeInMillis();
		DBGeoObject obj = new DBGeoObject(
				"Test Object",
				23,
				42,
				svcId,
				uid,
				"http://localhost",
				null,
				new Property[] {
					new Property("name1", "val1"),
					new Property("name2", "val2")
				},
				new String[] {
					"testTag1",
					"testTag2"
				});
		
		obj.setTags(new String[] {"testtag1", "testtag2"});
		
		geoObjManager.insert(obj);
		
		long objId = geoObjManager.getObjectId(svcId, uid);
		DBGeoObject obj2 = geoObjManager.getObjectbyId(objId);
		
		// check if the two objects  match
		//  (obj.getId() is uninitialized)
		assertEquals(obj.getLink(), obj2.getLink());
		assertEquals(obj.getSvcId(), obj2.getSvcId());
		assertEquals(obj.getTitle(), obj2.getTitle());
		assertEquals(obj.getUid(), obj2.getUid());
		assertEquals(obj.getXPos(), obj2.getXPos(), 0.00001);
		assertEquals(obj.getYPos(), obj2.getYPos(), 0.00001);
		_compareProperties(obj.getProperties(), obj2.getProperties());
		_compareTags(obj.getTags(), obj2.getTags());
		assertEquals(obj.getValid_until(), obj2.getValid_until());
	}
	
	@Test(expected= IndexOutOfBoundsException.class)
	public void fooTest() {
		throw new IndexOutOfBoundsException("TestException");
	}
	
	@Test
	public void testSimple() {
		IDBConnection dbConn = null;
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
				assertEquals(16.371422, outObj.getXPos(), 0.00001);
				assertEquals(48.198247, outObj.getYPos(), 0.00001);
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
	
	static void _compareProperties(Property expected[], Property actual[]) {
		if (expected.length != actual.length) {
			fail("_compareProperties: array length differs: (expected.length="+expected.length+", actual.length="+actual.length);
		}
		for (int i = 0; i < expected.length; i++) {
			boolean found = false;
			for (int j = 0; j < actual.length; j++) {
				if (actual[j].getName().equals(expected[i].getName())) {
					assertEquals(expected[i].getName(), actual[j].getName());
					assertEquals(expected[i].getValue(), actual[j].getValue());
					found = true;
				}
			}
			assertTrue("expected Property not found: "+expected[i], found);
		}
	}
	
	static void _compareTags(String[] expected, String[] actual) {
		Arrays.sort(expected, String.CASE_INSENSITIVE_ORDER);
		Arrays.sort(actual, String.CASE_INSENSITIVE_ORDER);
		
		assertArrayEquals(expected, actual);
	}

	@Before
	public void setUp() throws Exception {
		geoObjManager = GeoObjectManager.getInstance();
		tags= new String[]{"nacht", "see", "baum"};
		prop = new Property[]{new Property("owner", "lacitot")};
		svcId = ServiceManager.getInstance().select("example.com").getSvc_id();
		inObj = new DBGeoObject("Night", 21.440957, 48.427236, svcId, "test_panoramio", "http://www.panoramio.com/photo/1706188", java.sql.Timestamp.valueOf("2009-10-10 09:01:10"),prop, tags) ;
	}
	
	@After
	public void tearDown() throws Exception {
		// delete all created geoObjects
		Iterator<Long> it = createdObjects.iterator();
		while (it.hasNext()) {
			geoObjManager.delete(it.next());
		}
	}

}

