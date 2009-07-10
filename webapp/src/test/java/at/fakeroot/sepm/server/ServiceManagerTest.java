package at.fakeroot.sepm.server;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * tests the select-methods (primarily the sql-Strings) in the ServiceManger Class 
 * using the test-DB with svc_id = 15 in test_selectSvcId() and name = "example.com" 
 * in test_selectSvcName() 
 */
public class ServiceManagerTest  extends TestCase
{
	private ServiceManager svcManger;
	private DBService dbService;
	private DBService result;
	
	@Before
	public void setUp()
	{
		svcManger = new ServiceManager();
		dbService = new DBService(15, "example.com", "TestService", "http://www.example.com/", 
				"Test-Service", 12, "%description%", "images/service/wikipedia.png");
		dbService.setTags(new String[] {"stag1"});
	}
	
	@Test
	public void test_selectSvcId() 
	{
		try
		{
			if(DBConnection.staticIsTesting())
			{	
				result = svcManger.select(dbService.getSvc_id());
				assertNotNull(result);
				assertEquals(dbService.getName(), result.getName());
				assertEquals(dbService.getTitle(), result.getTitle());
				assertEquals(dbService.getHomepage(), result.getHomepage());
				assertEquals(dbService.getDescription(), result.getDescription());
				assertEquals(dbService.getSType_id(), result.getSType_id());
				assertEquals(dbService.getBubbleHTML(), result.getBubbleHTML());
				assertEquals(dbService.getThumbnail(), result.getThumbnail());
				assertEquals(dbService.getTags()[0], result.getTags()[0]);
				assertEquals(dbService.getTags().length, result.getTags().length);
			}
		}
		catch(Exception e)
		{}
	}
	
	@Test
	public void test_selectSvcName() 
	{
		try
		{
			if(DBConnection.staticIsTesting())
			{	
				result = svcManger.select(dbService.getName());
				assertNotNull(result);
				assertEquals(dbService.getSvc_id(), result.getSvc_id());
				assertEquals(dbService.getTitle(), result.getTitle());
				assertEquals(dbService.getHomepage(), result.getHomepage());
				assertEquals(dbService.getDescription(), result.getDescription());
				assertEquals(dbService.getSType_id(), result.getSType_id());
				assertEquals(dbService.getBubbleHTML(), result.getBubbleHTML());
				assertEquals(dbService.getThumbnail(), result.getThumbnail());
				assertEquals(dbService.getTags()[0], result.getTags()[0]);
				assertEquals(dbService.getTags().length, result.getTags().length);
			}
		}
		catch(Exception e)
		{}
	}
}
