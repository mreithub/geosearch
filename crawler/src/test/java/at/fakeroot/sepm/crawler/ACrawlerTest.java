package at.fakeroot.sepm.crawler;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;

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
			
			ArrayList<String> myList = new ArrayList<String>();
			myTestCrawler.parseStringIntoTags("Haus Mauer",myList,true);
			System.out.println(myList.get(0));
			assertEquals("Haus", myList.get(0));
			assertEquals("Mauer", myList.get(1));
			
			String tesURL= myTestCrawler.requestUrl("http://weristin.com/php/httptest");
			assertEquals(tesURL, "httptest");
			
		} catch (IOException e) {
			assertEquals(true, false);
			e.printStackTrace();
		}
		
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("setUp");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("tearDown");
	}
}


