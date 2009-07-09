package at.fakeroot.sepm.shared.client.serialize;
import junit.framework.TestCase;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * ClientGeoObject JUnit Test
 * @author JB
 *
 */
public class ClientGeoObjectTest extends TestCase {

	private double delta = 0.0001;
		
	@Test
	/**
	 * Test the constructor of the ClientGeoObject.
	 */
	public void testSimple(){
		int id=10;
		String title="testTitle";
		String url="pic.png";
		String[] tags={"tag1","tag2"};
		double xPos=1.1;
		double yPos=2.2;
		
		ClientGeoObject testObject=new ClientGeoObject(id,title,url,tags,xPos,yPos);
		
		assertNotNull(testObject);
		assertEquals(id, testObject.getId());
		assertEquals(title, testObject.getTitle());
		assertEquals(url, testObject.getImageUrl());
		assertEquals(tags[0], testObject.getTags()[0]);
		assertEquals(tags[1], testObject.getTags()[1]);
		assertEquals(xPos, testObject.getXPos(), delta);
		assertEquals(yPos, testObject.getYPos(), delta);
	}
}
