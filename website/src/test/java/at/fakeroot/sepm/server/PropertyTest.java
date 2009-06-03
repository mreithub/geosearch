package at.fakeroot.sepm.server;

import at.fakeroot.sepm.server.Property;
import junit.framework.TestCase;

/**
 * Property JUnit Test
 * @author JB
 *
 */
public class PropertyTest extends TestCase {

	public String getModulName(){
		return "at.fakeroot.sepm.server.Property";
	}
	
	public void testSimple(){
		String name="protTest";
		String value="valueTest";
		
		Property testObject = new Property(name,value);
		
		assertNotNull(testObject);
		assertEquals(name, testObject.getName());
		assertEquals(value, testObject.getValue());
	}
}
