package at.fakeroot.sepm.shared.client.serialize;


import org.junit.After;
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
	
	@After
	public void tearDown() throws Exception {
		System.out.println("tearDown");
	}
	
	
	@Test
	public void testGetters(){
		//StdTest
		assertNotNull(testObject);
		assertEquals(id, testObject.getId());
		assertEquals(title, testObject.getTitle());
		assertEquals(xPos, testObject.getXPos(), delta);
		assertEquals(yPos, testObject.getYPos(), delta);
	}
	
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
	
	@Test
	public void testTruncate(){
		String tags = "aaaabbbbccccddddeeeeffffgggghhhhiiiijjjjkkkkllllmmmmnnnnooooppppqqqqrrrrssssttttuuuuvvvvwwwwxxxxyyyyzzzz"+
				"AAAABBBBCCCCDDDDEEEEFFFFGGGGHHHHIIIIJJJJKKKKLLLLMMMMNNNNOOOOPPPPQQQQRRRRSSSSTTTTUUUUVVVVWWWWXXXXYYYYZZZZ"+
				"0123456789"+
				"10111213141516171819"+
				"20212223242526272829"; 

		assertEquals(GeoObject.truncate(tags, 255), tags.substring(0, 254)+"…");

	}
	
	@Test 
	public void testTruncateSmall(){
		System.out.println(GeoObject.truncate("aaa", 2));
		assertEquals(GeoObject.truncate("aaaaa", 2), "a…");
	}
	
	 @Test(expected = StringIndexOutOfBoundsException.class)
	 public void testTruncateNegative() {
	  GeoObject.truncate("abcde", -1);
	 }
	
}
