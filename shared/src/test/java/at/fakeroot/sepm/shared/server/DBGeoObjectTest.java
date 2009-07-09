package at.fakeroot.sepm.shared.server;

import java.sql.Timestamp;

import junit.framework.TestCase;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * DBGEoObject JUnit Test
 * @author JB
 *
 */
public class DBGeoObjectTest extends TestCase {

	private double delta = 0.0001;
	
	@Test
	public void testSimple(){
		int objID = 17;
		String title = "some title";
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
		
		DBGeoObject testObject = new DBGeoObject(objID, title, xPos, yPos, svc_id, uid, link, valid_until, properties, tags);
		
		
		assertNotNull(testObject);
		assertEquals(objID, testObject.getId());
		assertEquals(title, testObject.getTitle());
		assertEquals(xPos, testObject.getXPos(), delta);
		assertEquals(yPos, testObject.getYPos(), delta);
		assertEquals(svc_id, testObject.getSvcId(), delta);
		assertEquals(uid, testObject.getUid());
		assertEquals(link, testObject.getLink());
		assertEquals(valid_until, testObject.getValid_until());
		assertEquals(tags[0], testObject.getTags()[0]);
		assertEquals(tags[1], testObject.getTags()[1]);
		assertEquals(properties[0], testObject.getProperties()[0]);
		assertEquals(properties[1], testObject.getProperties()[1]);
	}
}
