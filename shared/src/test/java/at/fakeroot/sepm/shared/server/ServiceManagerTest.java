package at.fakeroot.sepm.shared.server;

import junit.framework.TestCase;

public class ServiceManagerTest extends TestCase{

	ServiceManager servManager;

	public void testSimple(){
		int wikiId=-1;
	
		try{
			DBService dbSvc = servManager.select("de.wikipedia.org");
			wikiId=dbSvc.getSvc_id();
	//		assertEquals(1, dbSvc.getSvc_id());
			assertEquals("de.wikipedia.org", dbSvc.getName());
			assertEquals("Wikipedia", dbSvc.getTitle());
			assertEquals("http://de.wikipedia.org/", dbSvc.getHomepage());
			assertEquals("Deutsche Wikipedia (freies Online-Lexicon)", dbSvc.getDescription());
			assertEquals(3, dbSvc.getSType_id());
			assertEquals("%summary%", dbSvc.getBubbleHTML());
			
		}catch(Exception e){
			fail("could not retrieve service by name:"+ e.getMessage());
			
		}
			try{
			DBService dbServ = servManager.select(wikiId);
			assertEquals("de.wikipedia.org", dbServ.getName());
			assertEquals("Wikipedia", dbServ.getTitle());
			assertEquals("http://de.wikipedia.org/", dbServ.getHomepage());
			assertEquals("Deutsche Wikipedia (freies Online-Lexicon)", dbServ.getDescription());
			assertEquals(3, dbServ.getSType_id());
			assertEquals("%summary%", dbServ.getBubbleHTML());

			}
		catch(Exception e){
			fail("could not retrieve service by id:"+ e.getMessage());
		}
		
	}

	@Override
	protected void setUp() throws Exception{
		servManager = ServiceManager.getInstance();
	}
}
