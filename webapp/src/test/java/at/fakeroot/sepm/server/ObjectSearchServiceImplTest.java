package at.fakeroot.sepm.server;
import static org.easymock.EasyMock.*;

import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;

import at.fakeroot.sepm.shared.client.serialize.ObjectDetails;
import at.fakeroot.sepm.shared.server.DBGeoObject;
import at.fakeroot.sepm.shared.server.Property;

/**
 * This test-class tests the getDetailHTML(long id) method of the ObjectSearchServiceImpl 
 * class. It's use is to fill in the specific Properties of an object into the bubblehtml.
 * In the bubblehtml are placeholders for the Properties, they always have the structure
 * %name_of_property%. These expressions will be replaced by the value of the Property inside 
 * this method. 
 */
public class ObjectSearchServiceImplTest 
{

	private IGeoObjectManager geoObjManagerMock;
	private IServiceManager svcMangerMock;
	private ObjectSearchServiceImpl objSearchServImpl;
	private ObjectDetails result;
	
	//Mock-Objects for the GeoObjectManager and ServiceManager (both DAO), it's not
	//necessary to use real values out of the DB
	@Before
	public void setUp()
	{
		geoObjManagerMock = createMock(IGeoObjectManager.class);
		svcMangerMock = createMock(IServiceManager.class);
		objSearchServImpl = new ObjectSearchServiceImpl(geoObjManagerMock, svcMangerMock);
	}
	
	//basic test: the bubblehtml just contains the %name_of_property%
	//%description% should be replaced by blablabla
	@Test
	public void test_getDetailHTML_1() throws Exception
	{
		expect(geoObjManagerMock.getObjectbyId(0)).andReturn(new DBGeoObject(0, "", 0, 15, 15, "", 
				"", null, new Property[] {new Property("description", "blablabla")}, new String[0] ));
		expect(svcMangerMock.select(15)).andReturn(new DBService(0, "", "", "", "",
				15, "%description%", ""));
		replay(geoObjManagerMock);
		replay(svcMangerMock);
		result = objSearchServImpl.getDetailHTML(0);
		assertEquals("blablabla", result.getHTMLString());
	}
	
	//more advanced test: there are also other %-signs in the bubblehtml, these shouldn't 
	//be replaced
	//%ich%description%hallo% should be replaced by %ichblablablahallo%
	@Test
	public void test_getDetailHTML_2() throws Exception
	{
		expect(geoObjManagerMock.getObjectbyId(0)).andReturn(new DBGeoObject(0, "", 0, 15, 15, "", 
				"", null, new Property[] {new Property("description", "blablabla")}, new String[0] ));
		expect(svcMangerMock.select(15)).andReturn(new DBService(0, "", "", "", "",
				15, "%ich%description%hallo%", ""));
		replay(geoObjManagerMock);
		replay(svcMangerMock);
		result = objSearchServImpl.getDetailHTML(0);
		assertEquals("%ichblablablahallo%", result.getHTMLString());
	}
	
	//more advanced test: there are also other %-signs in the bubblehtml, these shouldn't 
	//be replaced, just the "inner" %-signs should be replaced (this test checks, if the 
	//indices are really used properly)
	//%%description%% should be replaced by %blablabla%
	@Test
	public void test_getDetailHTML_3() throws Exception
	{
		expect(geoObjManagerMock.getObjectbyId(0)).andReturn(new DBGeoObject(0, "", 0, 15, 15, "", 
				"", null, new Property[] {new Property("description", "blablabla")}, new String[0] ));
		expect(svcMangerMock.select(15)).andReturn(new DBService(0, "", "", "", "",
				15, "%%description%%", ""));
		replay(geoObjManagerMock);
		replay(svcMangerMock);
		result = objSearchServImpl.getDetailHTML(0);
		assertEquals("%blablabla%", result.getHTMLString());
	}
}
