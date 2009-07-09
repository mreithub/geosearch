package at.fakeroot.sepm.shared.client.serialize;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * GeoObject JUnit Test
 * @author JB
 *
 */
public class GeoObjectTest {

	private double delta = 0.0001;
	
	public String getModulName(){
		return "at.fakeroot.sepm.client.serialize.GeoObject";
	}
	
	@Test
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
		assertEquals(xPos, testObject.getXPos(), delta);
		assertEquals(yPos, testObject.getYPos(), delta);
		
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
		assertEquals(xPos, testObject.getXPos(), delta);
		assertEquals(yPos, testObject.getYPos(), delta);
	}
}
