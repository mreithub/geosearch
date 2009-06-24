package at.fakeroot.sepm.shared.client.serialize;

import junit.framework.TestCase;

/**
 * ObjectDetails JUnit Test
 * @author JB
 *
 */
public class ObjectDetailsTest extends TestCase {

	public String getModulName(){
		return "at.fakeroot.sepm.client.serialize.ObjectDetails";
	}
	
	public void testSimple(){
		String HTMLString = "htmltest";
		String[] tags ={"tag1","tag2"};
		String link = "www.bla.com";
		String thumbnail = "thumbnail";
		String homepage = "homepage";
		
		ObjectDetails testObject = new ObjectDetails(HTMLString,tags, link, thumbnail, homepage);
		assertNotNull(testObject);
		assertEquals(HTMLString, testObject.getHTMLString());
		assertEquals(tags[0], testObject.getTags()[0]);
		assertEquals(tags[1], testObject.getTags()[1]);
	}
}
