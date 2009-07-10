package at.fakeroot.sepm.shared.client.serialize;


import org.junit.Test;
import static org.junit.Assert.*;

/**
 * ObjectDetails JUnit Test
 * @author JB
 *
 */
public class ObjectDetailsTest {

	/**
	 * Test the getters and the constructor
	 */
	@Test
	public void testGetters(){
		String HTMLString = "htmltest";
		String[] tags ={"tag1","tag2"};
		String link = "www.example.com";
		String thumbnail = "thumbnail";
		String homepage = "homepage";
		
		ObjectDetails testObject = new ObjectDetails(HTMLString,tags, link, thumbnail, homepage);
		
		assertEquals(HTMLString, testObject.getHTMLString());
		assertEquals(tags[0], testObject.getTags()[0]);
		assertEquals(tags[1], testObject.getTags()[1]);
	}
}
