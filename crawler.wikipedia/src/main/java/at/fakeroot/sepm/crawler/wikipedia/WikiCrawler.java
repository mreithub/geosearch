package at.fakeroot.sepm.crawler.wikipedia;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import at.fakeroot.sepm.crawler.ACrawler;
import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import at.fakeroot.sepm.shared.server.DBGeoObject;
import at.fakeroot.sepm.shared.server.Property;

/**
 * WikiCrawler searches for geotagged Wikipedia articles, to do so, it uses
 * the Wikipedia Geocoding Webservice http://www.geonames.org/export/wikipedia-webservice.html.
 * The summarys of the found articles (together with link, title, geoposition,...) are saved 
 * in the DB.  
 * @author RK
 */
public class WikiCrawler extends ACrawler
{
	//limit for the number of articles, for one request
	private static final int limit = 200;
	private Logger logger = Logger.getRootLogger();
	
	/**
	 * Constructor
	 * @throws IOException
	 * @throws NotBoundException
	 */
	public WikiCrawler() throws IOException, NotBoundException
	{
		super("de.wikipedia.org");
	}
	
	/**
	 * main-method to start crawling
	 * @param main
	 * @throws IOException
	 * @throws NotBoundException
	 */
	public static void main(String main[]) throws IOException, NotBoundException 
	{
		ACrawler crawler = new WikiCrawler();
		crawler.crawl();
	}

	/**
	 * the method scanns a defined area for Wikipedia articles 
	 * @param curBox the BoundingBox (area), which is scanned
	 */
	protected void crawlBox(BoundingBox curBox) 
	{
		ArrayList<DBGeoObject> dbGeoArray = new ArrayList<DBGeoObject>();
		//the request-url for wikipedia
		String request = "http://ws.geonames.org/wikipediaBoundingBoxJSON?north=" + curBox.getY2()
		+ "&south=" + curBox.getY1() + "&east=" + curBox.getX2() + "&west=" + curBox.getX1()
		+ "&lang=de&maxRows=200";
		String result = requestUrl(request);
		try
		{
			JSONObject jsonResult = new JSONObject(result);
			if(jsonResult.isNull("geonames"))
				return;
			JSONArray jsonResultArray  = jsonResult.getJSONArray("geonames");
			if(jsonResultArray.length() == 0)
				return;
			//the service doesn't give any information on how many articles altogether are in the area,
			//beside the ones, which are returned. so, if there are as many rows returned, as the maximum
			//(int limit) is, then the BoundingBox is divided into 4 BoundingBoxes, and for each of them, there will 
			//be a new request. 
			//  _____         __ __
			// |     | --> 1 |__|__| 3
			// |_____|     2 |__|__| 4
			
			if(jsonResultArray.length() >= limit)
			{
				crawlBox(new BoundingBox(curBox.getX1(), (curBox.getY1()+ curBox.getY2())/2, (curBox.getX1()+ curBox.getX2())/2, curBox.getY2()));
				crawlBox(new BoundingBox(curBox.getX1(), curBox.getY1(), (curBox.getX1()+ curBox.getX2())/2, (curBox.getY1()+ curBox.getY2())/2));
				crawlBox(new BoundingBox((curBox.getX1()+ curBox.getX2())/2, (curBox.getY1()+ curBox.getY2())/2, curBox.getX2(), curBox.getY2()));
				crawlBox(new BoundingBox((curBox.getX1()+curBox.getX2())/2, curBox.getY1(), curBox.getX2(), (curBox.getY1()+ curBox.getY2())/2));
			}
			else
			{
				for(int i = 0; i < jsonResultArray.length(); i++)
				{
					//read-out the values from JSONArray into a DBGeoObject
					jsonResult = jsonResultArray.getJSONObject(i);
					ArrayList<String> tags = new ArrayList<String>();
					String title = jsonResult.getString("title");
					parseStringIntoTags(title, tags, false);
					double xPos = jsonResult.getDouble("lng");
					double yPos = jsonResult.getDouble("lat");
					String link = "http://" + jsonResult.getString("wikipediaUrl");
					Property[] property = {new Property("summary", jsonResult.getString("summary"))};
					parseStringIntoTags(jsonResult.getString("summary"), tags, true);
					String[] dbObjTags = tags.toArray(new String[tags.size()]);
					dbGeoArray.add(new DBGeoObject(title, xPos, yPos, 0, title, link, null, property, dbObjTags));
				}
				//the found informations are saved in a DBGeoObject, which is going to be saved in the DB
				saveObject(dbGeoArray.toArray(new DBGeoObject[dbGeoArray.size()]));
			}
		}
		catch(Exception e)
		{
			logger.error("Exception in crawlBox() von WikipediaCrawler: ", e);
		}
		
	}
	
	

}
