package at.fakeroot.sepm.server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*; 

import at.fakeroot.sepm.server.NotFoundException;
import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import at.fakeroot.sepm.shared.client.serialize.ClientGeoObject;
import at.fakeroot.sepm.shared.client.serialize.SearchResult;
import at.fakeroot.sepm.shared.server.DBGeoObject;
import at.fakeroot.sepm.shared.server.Property;


public class GeoObjectManagerTest{
	private static double delta = 0.0001;
	IDBConnection db;
	IGeoObjectManager geoObjManager;
	String[] tags;
	Property[] prop;
	DBGeoObject inObj;
	int svcId;
	
	SortedSet<Long> createdObjects = new TreeSet<Long>();
	
	/**
	 * try to get an object that's already in the test database
	 */
	@Test
	public void testGetObjectById() throws SQLException, NotFoundException {
		long objId = geoObjManager.getObjectId(svcId, "test_karlskirche");
		DBGeoObject outObj = geoObjManager.getObjectbyId(objId);

		assertEquals("Karlskirche", outObj.getTitle());
		assertEquals("http://de.wikipedia.org/wiki/Wiener_Karlskirche", outObj.getLink());
		assertEquals(48.198247, outObj.getXPos(), delta);
		assertEquals(16.371422, outObj.getYPos(), delta);
		_compareProperties(new Property[]{new Property("description", "Beschreibung Karlskirche")}, outObj.getProperties());
		_compareTags(new String[]{"karlskirche", "kirche", "karlsplatz", "sehenswürdigkeit"}, outObj.getTags());
	}

	/**
	 * testing the search function
	 */
	@Test
	public void testSelect() {
		int countLimit = 200;
		SearchResult searchResult = geoObjManager.select(new String[] {"wahrzeichen"}, new BoundingBox(0,-90,360, 90), 50, countLimit);
		ArrayList<ClientGeoObject> objects = searchResult.getResults();
		
		assertEquals(9, searchResult.getResultCount());
		assertEquals(countLimit, searchResult.getCountLimit());
		
		String[] titles = new String[searchResult.getResultCount()];
		
		Iterator<ClientGeoObject> it = objects.iterator();
		
		int i = 0;
		while (it.hasNext()) {
			titles[i] = it.next().getTitle();
			i++;
		}
		_compareTags(
			new String[] {
				"Stephansdom",
				"Goldenes Dachl",
				"Festung Hohensalzburg",
				"Pöstlingberg",
				"Uhrturm",
				"Martinsturm",
				"Schloss Esterhazy",
				"Lindwurmbrunnen",
				"Rathaus"},
			titles);
	}

	/**
	 * - insert() a new GeoObject
	 * - try to get it via getObjectId() and getObjectById()
	 * - delete it via delete()
	 */
	@Test
	public void testInsertGetDelete() throws Exception {
		// give the object a unique id
		inObj.setUid(_createUniqUid());
		
		geoObjManager.insert(inObj);
		
		long objId = geoObjManager.getObjectId(inObj.getSvcId(), inObj.getUid()); 
		inObj.setId(objId);
		
		// the created object will be deleted by tearDown()
		createdObjects.add(objId);
		
		DBGeoObject outObj = geoObjManager.getObjectbyId(objId);
		
		// check if the two objects  match
		_compareGeoObjects(inObj, outObj);
	}
	
	@Test
	public void testUpdate() throws Exception {
		long objId = geoObjManager.getObjectId(svcId, "test_karlskirche");
		
		inObj.setId(objId);
		inObj.setUid("test_karlskirche"); // uid is not updatable
		
		// overwrite the geoObject test_karlskirche with the temporary one 
		geoObjManager.update(inObj);
		
		DBGeoObject outObj = geoObjManager.getObjectbyId(objId);
		
		_compareGeoObjects(inObj, outObj);
	}
	
	/**
	 * delete a geoObject from the database.
	 * We are using the testing mode of DBConnection which means
	 * the changes will be rolled back after calling dbConn.close()  
	 */
	@Test
	public void testDelete() throws SQLException, NotFoundException {
		long objId = geoObjManager.getObjectId(svcId, "test_karlskirche");
		// delete the GeoObject
		geoObjManager.delete(objId);
		
		// make sure it has been deleted
		try {
			geoObjManager.getObjectbyId(objId);
			
			// no NotFoundException was thrown => fail JUnit test
			fail ("geoObjectManager.delete() failed, the object still exists");
		}
		catch (NotFoundException e) {
			// object doesn't exist anymore => everything alright
		}

	}
	
	@Test(expected=NotFoundException.class)
	public void testDeleteNonExistent() throws Exception {
		geoObjManager.delete(-1);
	}

	@Test(expected=NotFoundException.class)
	public void testDeleteNonExistentUidAndSvcId() throws Exception {
		geoObjManager.delete(-1, "test_nonexistent");
	}

	/**
	 * 
	 */
	static void _compareGeoObjects(DBGeoObject expected, DBGeoObject actual) {
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getLink(), actual.getLink());
		assertEquals(expected.getSvcId(), actual.getSvcId());
		assertEquals(expected.getTitle(), actual.getTitle());
		assertEquals(expected.getUid(), actual.getUid());
		assertEquals(expected.getXPos(), actual.getXPos(), delta);
		assertEquals(expected.getYPos(), actual.getYPos(), delta);
		_compareProperties(expected.getProperties(), actual.getProperties());
		_compareTags(expected.getTags(), actual.getTags());
	}

	
	/**
	 * internal function to compare an object's properties
	 */
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
	
	/**
	 * internal function to compare an object's tags
	 */
	static void _compareTags(String[] expected, String[] actual) {
		Arrays.sort(expected, String.CASE_INSENSITIVE_ORDER);
		Arrays.sort(actual, String.CASE_INSENSITIVE_ORDER);
		
		assertArrayEquals(expected, actual);
	}
	
	static String _createUniqUid() {
		return "testObject"+Calendar.getInstance().getTimeInMillis();
	}

	@Before
	public void setUp() throws Exception {
		// we're always using the test database (there's a special jdbc.properties file in src/test/resources)
		assertTrue("Not using test database!", DBConnection.staticIsTesting());

		
		geoObjManager = GeoObjectManager.getInstance();
		
		// create a test object
		tags= new String[]{"nacht", "see", "baum"};
		prop = new Property[]{new Property("owner", "lacitot")};
		svcId = ServiceManager.getInstance().select("example.com").getSvcId();
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

