package at.fakeroot.sepm.shared.client.serialize;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * GeoObject JUnit Test
 * @author JB
 *
 */
public class GeoObjectTest extends TestCase  {

	private double delta;
	int id=1;
	String title;
	double xPos;
	double yPos;	
	
	GeoObject testObject;	
	
	@Before
	public void setUp(){
		delta = 0.0001;
		id=1;
		title = "GeoObjTestString";
		xPos = 1.1;
		yPos = 2.2;	
		testObject = new GeoObject(id, title,xPos,yPos);	
	}
	
	@After
	public void tearDown() throws Exception {
		System.out.println("tearDown");
	}
	
	
	@Test
	public void testGetTest_should(){
		//StdTest
		assertNotNull(testObject);
		assertEquals(id, testObject.getId());
		assertEquals(title, testObject.getTitle());
		assertEquals(xPos, testObject.getXPos(), delta);
		assertEquals(yPos, testObject.getYPos(), delta);
	}
	
	@Test
	public void testPutTest_should(){
		int _id=2;
		String _title= "GeoObjTestString2";
		double _xPos = 3.3;
		double _yPos = 4.4;
		String[] tags = new String[]{"tag1","tag2","tag3"}; 
		
		testObject.setId(_id);
		testObject.setTitle(_title);
		testObject.setXPos(_xPos);
		testObject.setYPos(_yPos);
		testObject.setTags(tags);
		
		assertEquals(_id, testObject.getId());
		assertEquals(_title, testObject.getTitle());
		assertEquals(_xPos, testObject.getXPos(), delta);
		assertEquals(_yPos, testObject.getYPos(), delta);
		assertEquals(tags[0],testObject.getTags()[0]);
		assertEquals(tags[1],testObject.getTags()[1]);
		assertEquals(tags[2],testObject.getTags()[2]);
	}
	
	@Test
	public void testTruncateTest_should(){
		String[] tags = new String[]{"aaaabbbbccccddddeeeeffffgggghhhhiiiijjjjkkkkllllmmmmnnnnooooppppqqqqrrrrssssttttuuuuvvvvwwwwxxxxyyyyzzzz"+
				"AAAABBBBCCCCDDDDEEEEFFFFGGGGHHHHIIIIJJJJKKKKLLLLMMMMNNNNOOOOPPPPQQQQRRRRSSSSTTTTUUUUVVVVWWWWXXXXYYYYZZZZ"+
				"0123456789"+
				"10111213141516171819"+
				"20212223242526272829"}; 
		testObject.setTags(tags);
		
		System.out.println("trunc: "+testObject.getTags()[0]);
		
		assertEquals(testObject.getTags()[0], tags[0].substring(0, tags[0].length()-3)+"...");
	}
	
}
