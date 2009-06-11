package at.fakeroot.sepm.shared.client.serialize;

import junit.framework.TestCase;

/**
 * GeoObject JUnit Test
 * @author JB
 *
 */
public class GeoObjectTest extends TestCase {

	public String getModulName(){
		return "at.fakeroot.sepm.client.serialize.GeoObject";
	}
	
	
	public void testSimple(){
		int id=1;
		String title = "GeoObjTestString";
		double xPos = 1.1;
		double yPos = 2.2;
		
		GeoObject testObject = new GeoObject(id, title,xPos,yPos);
		
		//StdTest
		assertNotNull(testObject);
		assertEquals(id, testObject.getId());
		assertEquals(title, testObject.getTitle());
		assertEquals(xPos, testObject.getXPos());
		assertEquals(yPos, testObject.getYPos());
		
		//Test with puts
		id=2;
		title = "GeoObjTestString2";
		xPos = 3.3;
		yPos = 4.4;
		testObject.setId(id);
		testObject.setTitle(title);
		testObject.setXPos(xPos);
		testObject.setYPos(yPos);
		
		assertNotNull(testObject);
		assertEquals(id, testObject.getId());
		assertEquals(title, testObject.getTitle());
		assertEquals(xPos, testObject.getXPos());
		assertEquals(yPos, testObject.getYPos());
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
