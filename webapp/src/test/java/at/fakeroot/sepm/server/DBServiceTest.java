package at.fakeroot.sepm.server;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * DBService JUnit test
 * @author Anca Cismasiu
 * */

public class DBServiceTest {
	
	private DBService serv;
	private int svc_id=10;
	private String name="myService.com";
	private String title="myService";
	private String homepage = "www.myService.com";
	private String description = "Describing myService";
	private int sTypeId = 1;
	private String bubbleHTML = "bubble";
	private String thumbnail = "www.myServiceThumbnail.com";
	
	
	@Test
	public void testGetter(){
		assertEquals(svc_id, serv.getSvc_id());
		assertEquals(name, serv.getName());
		assertEquals(title, serv.getTitle());
		assertEquals(homepage, serv.getHomepage());
		assertEquals(description, serv.getDescription());
		assertEquals(sTypeId, serv.getSType_id());
		assertEquals(bubbleHTML, serv.getBubbleHTML());
		assertEquals(thumbnail, serv.getThumbnail());
	}
	
	@Test
	public void testTags(){
		String[] tags = new String[]{"tag1","tag2"};
		
		serv.setTags(tags);
		assertArrayEquals(tags, serv.getTags());
	}
	
	@Before
	public void setUp(){
		serv = new DBService(svc_id, name, title, homepage, description, sTypeId, bubbleHTML, thumbnail);
	}
	
}

