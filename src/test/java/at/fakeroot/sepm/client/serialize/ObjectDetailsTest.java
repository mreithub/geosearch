package at.fakeroot.sepm.client.serialize;

import junit.framework.TestCase;

public class ObjectDetailsTest extends TestCase {

	public String getModulName(){
		return "at.fakeroot.sepm.client.serialize.ObjectDetails";
	}
	
	public void testSimple(){
		String HTMLString = "htmltest";
		String[] tags ={"tag1","tag2"};
		
		ObjectDetails testObject = new ObjectDetails(HTMLString,tags);
		assertNotNull(testObject);
		assertEquals(HTMLString, testObject.getHTMLString());
		assertEquals(tags[0], testObject.getTags()[0]);
		assertEquals(tags[1], testObject.getTags()[1]);
	}
}
