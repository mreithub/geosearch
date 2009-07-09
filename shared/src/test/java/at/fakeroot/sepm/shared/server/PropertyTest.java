package at.fakeroot.sepm.shared.server;

import junit.framework.TestCase;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Property JUnit Test
 * @author JB
 *
 */
public class PropertyTest extends TestCase {
	
	@Test
	public void testSimple(){
		String name="protTest";
		String value="valueTest";
		
		Property testObject = new Property(name,value);
		
		assertNotNull(testObject);
		assertEquals(name, testObject.getName());
		assertEquals(value, testObject.getValue());
	}
}
