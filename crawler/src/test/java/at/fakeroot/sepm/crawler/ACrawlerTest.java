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
				super("wiki_de");			
				
			}


			protected void crawlBox(BoundingBox curBox) {
			}
			
		}
		try {
			//Hier muss ein service verwendet werden den es sicher gibt!
			myTestCrawler myTestCrawler = new myTestCrawler("wiki_de");
			assertEquals(5, myTestCrawler.getSvcID());
			
			
			String tesURL= myTestCrawler.requestUrl("http://weristin.com/php/httptest");
			assertEquals(tesURL, "httptest");
			
		} catch (IOException e) {
			assertEquals(true, false);
			e.printStackTrace();
		}
		
	}
}


