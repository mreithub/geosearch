package at.fakeroot.sepm.shared.client.serialize;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * ClientGeoObject JUnit Test
 * @author JB
 *
 */
public class ClientGeoObjectTest {

	private double delta = 0.0001;
		
	@Test
	/**
	 * Test the getters and tthe Constructor
	 */
	public void testGetters(){
		int id=10;
		String title="testTitle";
		String url="pic.png";
		String[] tags={"tag1","tag2"};
		double xPos=1.1;
		double yPos=2.2;
		
		ClientGeoObject testObject=new ClientGeoObject(id,title,url,tags,xPos,yPos);
		
		assertEquals(id, testObject.getId());
		assertEquals(title, testObject.getTitle());
		assertEquals(url, testObject.getImageUrl());
		assertEquals(xPos, testObject.getXPos(), delta);
		assertEquals(yPos, testObject.getYPos(), delta);
		assertArrayEquals(tags, testObject.getTags());
	}
}
