package at.fakeroot.sepm.shared.server;

import java.sql.Timestamp;

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
		int objID = 17;
		String titel = "ein toller titel";
		double xPos = 15.458;
		double yPos = 54.302;
		int svc_id = 1;
		String uid = "unique ID (URL)";
		String link ="link1";
		Timestamp valid_until = new Timestamp(0);
		Property pTest1 = new Property("p_name1","p_value1");
		Property pTest2 = new Property("p_name2","p_value2");
		Property[] properties = {pTest1,pTest2};
		String[] tags = {"tag1","tag2"};
		
		DBGeoObject testObject = new DBGeoObject(objID, titel, xPos, yPos, svc_id, uid, link, valid_until, properties, tags);
		
		
		assertNotNull(testObject);
		assertEquals(objID, testObject.getId());
		assertEquals(titel, testObject.getTitel());
		assertEquals(xPos, testObject.getXPos());
		assertEquals(yPos, testObject.getYPos());
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
