package at.fakeroot.sepm.server;

import static at.fakeroot.sepm.server.GeoObjectManagerTest.*;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class ServiceManagerTest {

	ServiceManager servManager;

	@Test
	public void testGetServiceInfo_shouldSucceed() throws FileNotFoundException, IOException, SQLException{
		int svcId=-1;
	
		if (!DBConnection.staticIsTesting()) return;
		
		DBService dbSvc = servManager.select("example.com");
		svcId=dbSvc.getSvc_id();

		assertEquals("example.com", dbSvc.getName());
		assertEquals("TestService", dbSvc.getTitle());
		assertEquals("http://www.example.com/", dbSvc.getHomepage());
		assertEquals("Test-Service", dbSvc.getDescription());
		//assertEquals(3, dbSvc.getSType_id());
		assertEquals("%description%", dbSvc.getBubbleHTML());
		
		// query the service a second time, but this time with the other select function
		DBService dbSvc2 = servManager.select(svcId);
		assertEquals(dbSvc.getName(), dbSvc2.getName());
		assertEquals(dbSvc.getTitle(), dbSvc2.getTitle());
		assertEquals(dbSvc.getHomepage(), dbSvc2.getHomepage());
		assertEquals(dbSvc.getDescription(), dbSvc2.getDescription());
		assertEquals(dbSvc.getSType_id(), dbSvc2.getSType_id());
		assertEquals(dbSvc.getBubbleHTML(), dbSvc2.getBubbleHTML());
		_compareTags(dbSvc.getTags(), dbSvc2.getTags());
	}

	@Before
	public void setUp() throws Exception{
		servManager = ServiceManager.getInstance();
	}
}
