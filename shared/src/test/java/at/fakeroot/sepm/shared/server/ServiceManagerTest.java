package at.fakeroot.sepm.shared.server;

import java.sql.Statement;

import junit.framework.TestCase;

public class ServiceManagerTest extends TestCase{

	ServiceManager servManager;

	public void testSimple(){
		
		try{
			DBService dbServ = servManager.select(1);
			assertEquals("de.wikipedia.org", dbServ.getName());
			assertEquals("Wikipedia", dbServ.getTitle());
			assertEquals("http://de.wikipedia.org/", dbServ.getHomepage());
			assertEquals("Deutsche Wikipedia (freies Online-Lexicon)", dbServ.getDescription());
			assertEquals(3, dbServ.getSType_id());
			assertEquals("<div>%description%</div>", dbServ.getBubbleHTML());

			}
		catch(Exception e){
			fail("could not retrieve service by id:"+ e.getMessage());
		}
		
		try{
			DBService dbSvc = servManager.select("de.wikipedia.org");
			assertEquals(1, dbSvc.getSvc_id());
			assertEquals("de.wikipedia.org", dbSvc.getName());
			assertEquals("Wikipedia", dbSvc.getTitle());
			assertEquals("http://de.wikipedia.org/", dbSvc.getHomepage());
			assertEquals("Deutsche Wikipedia (freies Online-Lexicon)", dbSvc.getDescription());
			assertEquals(3, dbSvc.getSType_id());
			assertEquals("<div>%description%</div>", dbSvc.getBubbleHTML());
			
		}catch(Exception e){
			fail("could not retrieve service by name:"+ e.getMessage());
			
		}
	}

	@Override
	protected void setUp() throws Exception{
		servManager = ServiceManager.getInstance();
	}
}
