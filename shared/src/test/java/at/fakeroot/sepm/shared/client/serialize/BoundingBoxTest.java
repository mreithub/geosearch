package at.fakeroot.sepm.shared.client.serialize;
import junit.framework.TestCase;
/**
 * BoundingBox JUnitTest
 * @author RK
 */
public class BoundingBoxTest extends TestCase
{

	public String getModulName()
	{
		return ("at.fakeroot.sepm.client.serialize.BoundingBox");
	}
	
	public void testSimple(){
		double x1 = 14.897;
		double x2 = 17.986;
		double y1 = 45.789;
		double y2 = 46.67;
		
		BoundingBox testObject = new BoundingBox(x1,y1,x2,y2);
		
		assertNotNull(testObject);
		assertEquals(x1, testObject.getX1());
		assertEquals(x2, testObject.getX2());
		assertEquals(y1, testObject.getY1());
		assertEquals(y2, testObject.getY2());
	}
}
