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
	 * This function tests if the insertion of a new object works.
	 */
	public void testInsert() throws Exception
	{
		mockManager = createStrictMock(IGeoObjectManager.class);
		IGeoSaveImpl geoSave = new IGeoSaveImpl(mockManager);
		
		DBGeoObject geoObj = new DBGeoObject(3, "abc", 1.11, 2.22, 17, "uid", "link", null, null, null);
		
		expect(mockManager.getObjectId(eq(17), eq("uid"))).andThrow(new NotFoundException());
		mockManager.insert(geoObj);
		
		replay(mockManager);
		geoSave.saveObject(geoObj);
		verify(mockManager);
	}
}
