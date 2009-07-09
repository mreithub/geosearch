package at.fakeroot.sepm.crawler;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import at.fakeroot.sepm.shared.client.serialize.BoundingBox;


public class ACrawlerTest {
	private ACrawler aCrawler;
	
	/*
	@Test
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
	*/
	
	@Test
	public void testSplitChar(){
		System.out.println("splitCharTest");
		ArrayList<String> myList = new ArrayList<String>();
		aCrawler.parseStringIntoTags("Haus Mauer der",myList,true);
		assertEquals("Haus", myList.get(0));
		assertEquals("Mauer", myList.get(1));		
		
		assertTrue(!myList.contains("der"));
	}
	
	@Test
	public void testRequestUrl(){
		String tesURL= aCrawler.requestUrl("http://weristin.com/php/httptest");
		assertEquals(tesURL, "httptest");
	}
	
	
	@Before
	public void setUp() throws Exception {
		System.out.println("setUp");
		
		aCrawler = new ACrawler("test"){

			@Override
			protected void crawlBox(BoundingBox curBox) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("tearDown");
	}
}


