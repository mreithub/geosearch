package at.fakeroot.sepm.shared.client.serialize;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * GeoObject JUnit Test
 * @author JB
 *
 */
public class GeoObjectTest  {

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
	
	/**
	 * testing GeoObject's getters
	 */
	@Test
	public void testGetters(){
		//StdTest
		assertNotNull(testObject);
		assertEquals(id, testObject.getId());
		assertEquals(title, testObject.getTitle());
		assertEquals(xPos, testObject.getXPos(), delta);
		assertEquals(yPos, testObject.getYPos(), delta);
	}
	
	/**
	 * testing GeoObejct's setters
	 */
	@Test
	public void testSetters(){
		int _id=2;
		String _title= "GeoObjTestString2";
		double _xPos = 3.3;
		double _yPos = 4.4;
		String[] tags = new String[1000]; 
		
		for(int i=0;i<1000;i++)
			tags[i]="a";
		
		
		testObject.setId(_id);
		testObject.setTitle(_title);
		testObject.setXPos(_xPos);
		testObject.setYPos(_yPos);
		testObject.setTags(tags);
		
		assertEquals(_id, testObject.getId());
		assertEquals(_title, testObject.getTitle());
		assertEquals(_xPos, testObject.getXPos(), delta);
		assertEquals(_yPos, testObject.getYPos(), delta);
		for(int i=0;i<1000;i++)
			assertEquals(tags[i],testObject.getTags()[i]);

		
		String[] tags2=new String[1000];
		for(int i=0;i<1000;i++)
			tags2[i]="b";
		testObject.setTags(tags2);
		for(int i=0;i<1000;i++)
			assertEquals(tags2[i],testObject.getTags()[i]);
	}
	
	/**
	 * testing the setTags() function
	 */
	@Test
	public void testSetTags() {
		String[] tags = new String[4];
		// init tags
		tags[0] = "abcde";
		for (int i = 0; i < 255; i++) {
			tags[1] += "a";
		}
		for (int i = 0; i < 256; i++) {
			tags[2] += "b";
		}
		for (int i = 0; i < 300; i++) {
			tags[3] += "c";
		}
		
		testObject.setTags(tags);
		String outTags[] = testObject.getTags();
		assertEquals(4, outTags.length);
		// the empty tag should have been deleted
		assertEquals(tags[0], outTags[0]);
		assertEquals(tags[1], outTags[1]);
		assertEquals(tags[2].substring(0,254)+"…", outTags[2]);
		assertEquals(tags[3].substring(0,254)+"…", outTags[3]);
	}
	
	/**
	 * test the truncate function
	 */
	@Test
	public void testTruncate(){
		String str = "abcde";

		// len is long enough => shouldn't truncate
		assertEquals(str, GeoObject.truncate(str, str.length()));
		
		// len is too short => truncate
		assertEquals("abc…", GeoObject.truncate(str, str.length()-1));
		
		// len == 1 => truncate returns just the first character
		assertEquals("a", GeoObject.truncate(str, 1));
	}
	
	/**
	 * test truncate with a negative length (which should cause an exception)
	 */
	@Test(expected = StringIndexOutOfBoundsException.class)
	public void testTruncateNegative() {
		GeoObject.truncate("abcde", -1);
	}
	
}
