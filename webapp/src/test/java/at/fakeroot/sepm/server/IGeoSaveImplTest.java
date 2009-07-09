package at.fakeroot.sepm.server;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;


import org.junit.Test;

import at.fakeroot.sepm.shared.server.DBGeoObject;

import at.fakeroot.sepm.server.NotFoundException;

public class IGeoSaveImplTest
{
	private IGeoObjectManager mockManager;
	
	@Test
	/**
	 * This function tests if the insertion of a new object works. Uses Mock objects.
	 */
	public void testInsert() throws Exception
	{
		//Create the Mock object.
		mockManager = createStrictMock(IGeoObjectManager.class);
		IGeoSaveImpl geoSave = new IGeoSaveImpl(mockManager);
		
		//Create the geo object to test with.
		DBGeoObject geoObj = new DBGeoObject(3, "abc", 1.11, 2.22, 17, "uid", "link", null, null, null);
		
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
	public void testUpdate() throws Exception
	{
		//Create the Mock object.
		mockManager = createStrictMock(IGeoObjectManager.class);
		IGeoSaveImpl geoSave = new IGeoSaveImpl(mockManager);
		
		//Create the geo object to test with.
		DBGeoObject geoObj = new DBGeoObject(11, "abc", 1.11, 2.22, 17, "uid", "link", null, null, null);
		
		//Define the expected function calls to the Mock object.
		expect(mockManager.getObjectId(eq(17), eq("uid"))).andReturn((long)11);
		mockManager.update(geoObj);
		
		//Test if the expected function calls are actually made when the test runs.
		replay(mockManager);
		geoSave.saveObject(geoObj);
		verify(mockManager);
	}
}
