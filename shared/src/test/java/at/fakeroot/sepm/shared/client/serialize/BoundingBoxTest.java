package at.fakeroot.sepm.shared.client.serialize;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * BoundingBox JUnitTest
 * @author RK
 */
public class BoundingBoxTest
{
	private double delta = 0.0001;	
	
	@Test
	/**
	 * Test the initzialisation of the bounding box.
	 */
	public void testConstructor1(){
		double x1 = 14.897;
		double x2 = 17.986;
		double y1 = 45.789;
		double y2 = 46.67;
		
		BoundingBox testObject = new BoundingBox(x1,y1,x2,y2);
		
		assertNotNull(testObject);
		assertEquals(x1, testObject.getX1(), delta);
		assertEquals(x2, testObject.getX2(), delta);
		assertEquals(y1, testObject.getY1(), delta);
		assertEquals(y2, testObject.getY2(), delta);
	}
	
	@Test
	/**
	 * Test the initzialisation of the bounding box, and check if (x1, y1) actually
	 * is the lower, left corner of the box.
	 */
	public void testConstructor2(){
		double x1 = 17.986;
		double x2 = 14.897;
		double y1 = 46.67;
		double y2 = 45.789;
		
		BoundingBox testObject = new BoundingBox(x1,y1,x2,y2);
		
		assertNotNull(testObject);
		assertEquals(x2, testObject.getX1(), delta);
		assertEquals(x1, testObject.getX2(), delta);
		assertEquals(y2, testObject.getY1(), delta);
		assertEquals(y1, testObject.getY2(), delta);
	}	
}
