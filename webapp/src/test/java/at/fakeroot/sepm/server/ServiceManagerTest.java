package at.fakeroot.sepm.server;


import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

/**
 * tests the select-methods (primarily the sql-Strings) in the ServiceManger Class 
 * using the test-DB with svc_id = 15 in test_selectSvcId() and name = "example.com" 
 * in test_selectSvcName() 
 */
public class ServiceManagerTest
{
	private ServiceManager svcManager;
	private DBService dbService;
	private DBService result;
	
	@Before
	public void setUp() throws SQLException, IOException
	{
		svcManager = new ServiceManager();
		String svcName = "example.com";
		// the service id is generated automatically by the database => first request it
		int svcId = svcManager.select(svcName).getSvc_id();
		// initialize a DBService objects with the data of the test service in the database 
		dbService = new DBService(svcId, svcName, "TestService", "http://www.example.com/", 
				"Test-Service", 12, "%description%", "images/service/wikipedia.png");
		dbService.setTags(new String[] {"stag1"});
	}
	
	/**
	 * Select by service ID
	 */
	@Test
	public void test_selectBySvcId() throws FileNotFoundException, IOException, SQLException 
	{
		if(DBConnection.staticIsTesting())
		{	
			result = svcManager.select(dbService.getSvc_id());
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
	
	/**
	 * select by service name
	 */
	@Test
	public void test_selectBySvcName() throws FileNotFoundException, IOException, SQLException 
	{
		if(DBConnection.staticIsTesting())
		{	
			result = svcManager.select(dbService.getName());
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
}
