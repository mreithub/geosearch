package at.fakeroot.sepm.server;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import org.junit.Before;
import org.junit.Test;

import at.fakeroot.sepm.shared.server.DBGeoObject;

public class IGeoSaveImplTest
{
	private GeoObjectManager mockManager;
	private IGeoSaveImpl geoSave;
	private DBGeoObject geoObj;
	
	@Before
	public void setUp()
	{
		//Create the Mock object.
		mockManager = createStrictMock(GeoObjectManager.class);
		geoSave = new IGeoSaveImpl(mockManager);
		
		//Create the geo object to test with.
		geoObj = new DBGeoObject(11, "abc", 1.11, 2.22, 17, "uid", "link", null, null, null);		
	}
	
	@Test
	/**
	 * This function tests if the insertion of a new object works. Uses Mock objects.
	 */
	public void testInsertObject() throws Exception
	{		
		//Define the expected function calls to the Mock object.
		expect(mockManager.getObjectId(eq(17), eq("uid"))).andThrow(new NotFoundException());
		mockManager.insert(geoObj);
		
		//Test if the expected function calls are actually made when the test runs.
		replay(mockManager);
		geoSave.saveObject(geoObj);
		verify(mockManager);
	}
	
	@Test
	/**
	 * This function tests if the update of an already existing object works. Uses Mock objects.
	 */
	public void testUpdateObject() throws Exception
	{
		//Define the expected function calls to the Mock object.
		expect(mockManager.getObjectId(eq(17), eq("uid"))).andReturn((long)11);
		mockManager.update(geoObj);
		
		//Test if the expected function calls are actually made when the test runs.
		replay(mockManager);
		geoSave.saveObject(geoObj);
		verify(mockManager);
	}
}
