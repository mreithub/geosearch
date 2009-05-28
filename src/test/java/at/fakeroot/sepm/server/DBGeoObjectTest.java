package at.fakeroot.sepm.server;

import junit.framework.TestCase;

/**
 * DBGEoObject JUnit Test
 * @author JB
 *
 */
public class DBGeoObjectTest extends TestCase {

	public String getModulName(){
		return "at.fakeroot.sepm.server.DBGeoObject";
	}
	
	public void testSimple(){
		int svc_id = 1;
		int uid = 2 ;
		String link ="link1";
		String valid_until ="until1";
		Property pTest1 = new Property("p_name1","p_value1");
		Property pTest2 = new Property("p_name2","p_value2");
		Property[] properties = {pTest1,pTest2};
		String[] tags = {"tag1","tag2"};
		
		DBGeoObject testObject = new DBGeoObject(svc_id,uid,link,valid_until,properties,tags);
		
		
		assertNotNull(testObject);
		assertEquals(svc_id, testObject.getSvc_id());
		assertEquals(uid, testObject.getUid());
		assertEquals(link, testObject.getLink());
		assertEquals(valid_until, testObject.getValid_until());
		assertEquals(tags[0], testObject.getTags()[0]);
		assertEquals(tags[1], testObject.getTags()[1]);
		assertEquals(properties[0], testObject.getProperties()[0]);
		assertEquals(properties[1], testObject.getProperties()[1]);
	}
}
