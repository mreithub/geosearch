package at.fakeroot.sepm.crawler;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import at.fakeroot.sepm.shared.server.DBGeoObject;

public class ACrawlerTest {
	
	private CrawlerOutput crawlerOutput;
	
	@Test
	public void testparseStringIntoTags() throws IOException, NotBoundException{
		class TestCrawler extends ACrawler {			
			public TestCrawler(String svcName, CrawlerOutput output)
					throws IOException, NotBoundException {
				super(svcName, output);
			}
			@Override
			protected void crawlBox(BoundingBox curBox) {
			}

		}
		ACrawler aCrawler = new TestCrawler("TestOutput",crawlerOutput);
		
		ArrayList<String> myList = new ArrayList<String>();
		aCrawler.parseStringIntoTags("Haus Mauer der",myList,true);
		assertEquals("Haus", myList.get(0));
		assertEquals("Mauer", myList.get(1));		
		
		assertTrue(!myList.contains("der"));
	}
	
	@Test
	public void testSubBox()  throws Exception{
		class TestCrawler extends ACrawler {
			
			private int tc=0;
			
			public TestCrawler(String svcName, CrawlerOutput output)
					throws IOException, NotBoundException {
				super(svcName, output);
			}

			@Override
			protected void crawlBox(BoundingBox curBox) {
				
				if(tc==0){
					assertEquals(curBox.getX1(),-1.0,0.0001);
					assertEquals(curBox.getY1(),0.0,0.0001);
					assertEquals(curBox.getX2(),-0.5,0.0001);
					assertEquals(curBox.getY2(),0.5,0.0001);
				}else if(tc==1){
					assertEquals(curBox.getX1(),-0.5,0.0001);
					assertEquals(curBox.getY1(),0.0,0.0001);
					assertEquals(curBox.getX2(),0.0,0.0001);
					assertEquals(curBox.getY2(),0.5,0.0001);
				}else if(tc==2){
					assertEquals(curBox.getX1(),0.0,0.0001);
					assertEquals(curBox.getY1(),0.0,0.0001);
					assertEquals(curBox.getX2(),0.5,0.0001);
					assertEquals(curBox.getY2(),0.5,0.0001);
				}else if(tc==3){
					assertEquals(curBox.getX1(),0.5,0.0001);
					assertEquals(curBox.getY1(),0.0,0.0001);
					assertEquals(curBox.getX2(),1.0,0.0001);
					assertEquals(curBox.getY2(),0.5,0.0001);
				}else if(tc==4){
					assertEquals(curBox.getX1(),-1.0,0.0001);
					assertEquals(curBox.getY1(),0.5,0.0001);
					assertEquals(curBox.getX2(),-0.5,0.0001);
					assertEquals(curBox.getY2(),1.0,0.0001);
				}else if(tc==5){
					assertEquals(curBox.getX1(),-0.5,0.0001);
					assertEquals(curBox.getY1(),0.5,0.0001);
					assertEquals(curBox.getX2(),0.0,0.0001);
					assertEquals(curBox.getY2(),1.0,0.0001);
				}else if(tc==6){
					assertEquals(curBox.getX1(),0.0,0.0001);
					assertEquals(curBox.getY1(),0.5,0.0001);
					assertEquals(curBox.getX2(),0.5,0.0001);
					assertEquals(curBox.getY2(),1.0,0.0001);
				}else if(tc==7){
					assertEquals(curBox.getX1(),0.5,0.0001);
					assertEquals(curBox.getY1(),0.5,0.0001);
					assertEquals(curBox.getX2(),1.0,0.0001);
					assertEquals(curBox.getY2(),1.0,0.0001);
				}
				
				
				tc++;
			}

		}
		ACrawler aCrawler = new TestCrawler("TestOutput",crawlerOutput);	
		aCrawler.crawl();
	}
	
	@Test
	public void testSubBoxJump() throws IOException, NotBoundException{
		class TestCrawler extends ACrawler {
			private int tc=0;
			BoundingBox oldBox=null;
			
			public TestCrawler(String svcName, CrawlerOutput output)
					throws IOException, NotBoundException {
				super(svcName, output);
			}

			@Override
			protected void crawlBox(BoundingBox curBox) {
				
				if(tc%4!=0 && oldBox!=null){
					assertEquals(curBox.getX1(), oldBox.getX2(),0.0001);
				}else if(tc%4==0 && oldBox!=null){
					assertEquals(curBox.getY1(), oldBox.getY2(),0.0001);
				}
				
				tc++;
				oldBox=curBox;
			}

		}
		ACrawler aCrawler = new TestCrawler("TestOutput",crawlerOutput);	
		aCrawler.crawl();
	}
	
	@Test
	public void testRequestUrl() throws IOException, NotBoundException{
		class TestCrawler extends ACrawler {			
			public TestCrawler(String svcName, CrawlerOutput output)
					throws IOException, NotBoundException {
				super(svcName, output);
			}
			@Override
			protected void crawlBox(BoundingBox curBox) {
			}

		}
		
		ACrawler aCrawler = new TestCrawler("TestOutput",crawlerOutput);
		
		String tesURL= aCrawler.requestUrl("http://weristin.com/php/httptest");
		assertEquals(tesURL, "httptest");
	}
	
	
	@Before
	public void setUp() throws Exception {
		System.out.println("setUp");
		
		crawlerOutput=new CrawlerOutput(){

			@Override
			public String getSplitChars() {
				return " \r\n.,!?\"`\\";
			}
			@Override
			public String[] getStopWords() {
				return new String[]{"der","die","das","in","ein","aus"};
			}
			@Override
			public void saveObjects(DBGeoObject[] newObjects) {
			}			
		};
		
		
		
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("tearDown");
	}
}



