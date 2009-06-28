package at.fakeroot.sepm.server;

import junit.framework.TestCase;

/**
 * DBService JUnit test
 * @author Anca Cismasiu
 * */

public class DBServiceTest extends TestCase {

	public void testSimple(){
		
		int svc_id=10;
		String name="myService.com";
		String title="myService";
		String homepage = "www.myService.com";
		String description = "Describing myService";
		int sTypeId = 1;
		String bubbleHTML = "bubble";
		String thumbnail = "www.myServiceThumbnail.com";
		
		DBService serv = new DBService(svc_id, name, title, homepage, description, sTypeId, bubbleHTML, thumbnail);
		
		assertEquals(svc_id, serv.getSvc_id());
		assertEquals(name, serv.getName());
		assertEquals(title, serv.getTitle());
		assertEquals(homepage, serv.getHomepage());
		assertEquals(description, serv.getDescription());
		assertEquals(sTypeId, serv.getSType_id());
		assertEquals(bubbleHTML, serv.getBubbleHTML());
		
	}
}

