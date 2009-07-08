package at.fakeroot.sepm.crawler;

import java.io.IOException;
import java.rmi.NotBoundException;
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
	
	public void testSimple() throws IOException, NotBoundException {
		class myTestCrawler extends ACrawler{

			public myTestCrawler(String svcName) throws IOException, NotBoundException {
				// TODO use test CrawlerOutput class
				super(svcName);			
				
			}


			protected void crawlBox(BoundingBox curBox) {
			}
			
		}

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


