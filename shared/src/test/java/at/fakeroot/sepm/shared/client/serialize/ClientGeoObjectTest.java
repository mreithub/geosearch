package at.fakeroot.sepm.shared.client.serialize;

import junit.framework.TestCase;

/**
 * ClientGeoObject JUnit Test
 * @author JB
 *
 */
public class ClientGeoObjectTest extends TestCase {

	
	public String getModulName(){
		return "at.fakeroot.sepm.client.serialize.ClientGeoObject";
	}
	
	public void testSimple(){
		int id=10;
		String titel="testTitel";
		String url="bild.png";
		String[] tags={"tag1","tag2"};
		double xPos=1.1;
		double yPos=2.2;
		

		ClientGeoObject testObject=new ClientGeoObject(id,titel,url,tags,xPos,yPos);
		
		assertNotNull(testObject);
		assertEquals(id, testObject.getId());
		assertEquals(titel, testObject.getTitel());
		assertEquals(url, testObject.getImageUrl());
		assertEquals(tags[0], testObject.getTags()[0]);
		assertEquals(tags[1], testObject.getTags()[1]);
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
