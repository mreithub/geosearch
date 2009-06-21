package at.fakeroot.sepm.crawler;

import java.io.IOException;

import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import junit.framework.TestCase;

public class ACrawlerTest extends TestCase {
	public String getModulName()
	{
		return ("at.fakeroot.sepm.crawler.ACrawlerTest");
	}
	
	public void testSimple(){
		class myTestCrawler extends ACrawler{

			public myTestCrawler(String svcName) throws IOException {
				super(svcName);			
				
			}


			protected void crawlBox(BoundingBox curBox) {
			}
			
		}
		try {
			//Hier muss ein service verwendet werden den es sicher gibt!
			myTestCrawler myTestCrawler = new myTestCrawler("de.wikipedia.org");
			// diese assertion kann nicht sichergestellt werden, da der Test keinen einfluss auf die Datenbank hat
			//assertEquals(5, myTestCrawler.getSvcID());
			
			
			String tesURL= myTestCrawler.requestUrl("http://weristin.com/php/httptest");
			assertEquals(tesURL, "httptest");
			
		} catch (IOException e) {
			assertEquals(true, false);
			e.printStackTrace();
		}
		
	}
}


