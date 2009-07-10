bpackage at.fakeroot.sepm.shared.server;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Property JUnit Test
 * @author JB
 *
 */
public class PropertyTest {
	
	/**
	 * test the getters and the constructor
	 */
	@Test
	public void testGetters(){
		String name="propTest";
		String value="valueTest";
sv
		Property testObject = new Property(name,value);

		assertEquals(name, testObject.getName());
		assertEquals(value, testObject.getValue());
	}
}
