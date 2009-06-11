package at.fakeroot.sepm.crawler.panoramio;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import at.fakeroot.sepm.crawler.ACrawler;
import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import at.fakeroot.sepm.shared.server.DBGeoObject;
import at.fakeroot.sepm.shared.server.Property;

/**
 * Class representing a crawler for the website panoramio.com. 
 * The maximum number of photos we can retrieve in one query is 100.
 * */

public class PanoramioCrawler extends ACrawler {

	/**
	 * Private constructor, called by the main method in the class 
	 * @throws IOException, NotBoundException
	 * */
	private PanoramioCrawler() throws IOException, NotBoundException {
		super("panoramio.com");
	}
		
	/**
	 * Method used for retrieving all photos in a bounding box, in a 100-step loop. The photos are then saved to the DB as DBGeoObjects 
	 * @param curBox the BoundingBox to be crawled 
	 * */
	@Override
	protected void crawlBox(BoundingBox curBox) {
		ArrayList<DBGeoObject> foundObjects = new ArrayList<DBGeoObject>();
		int from=0, to=0, count=0;
		String url="";
		String responseStr = "";
		
		while(to<=count){
			url = "http://www.panoramio.com/map/get_panoramas.php?order=popularity&set=public" +
					"&from=" + from + "&to=" + to +
					"&minx=" + curBox.getX1()+ "&miny=" + curBox.getY1()+ 
					"&maxx=" + curBox.getX2()+ "&maxy=" + curBox.getY2()+ "&size=small";
		
			responseStr = requestUrl(url);
		
			try {
				JSONObject jsonResponse = new JSONObject(responseStr);
				JSONArray photoArray = jsonResponse.getJSONArray("photos");
				
				for(int i=0; i<=photoArray.length(); i++){
					
					JSONObject jsonObj = new JSONObject(photoArray.getJSONObject(i));
					
					Property[] tmpProp = new Property[2];
					tmpProp[0]= new Property("owner", jsonObj.getString("owner_name"));
					tmpProp[1]= new Property("photo_url", jsonObj.getString("photo_url"));
					
					ArrayList<String> tags=new ArrayList<String>();
					parseStringIntoTags( jsonObj.getString("photo_title"), tags, true);
					
					DBGeoObject tmpObj = new DBGeoObject(jsonObj.getString("photo_title"), 
														jsonObj.getDouble("longitude"),
														jsonObj.getDouble("latitude"), 
														getSvcID(), 
														JSONObject.numberToString(jsonObj.getInt("photo_id")),
														jsonObj.getString("photo_url"),
														null, 
														tmpProp,
														(String [])tags.toArray() );
					foundObjects.add(tmpObj);
				}
				
				saveObject((DBGeoObject[])foundObjects.toArray());
			
				count=jsonResponse.getInt("count");
				from=to+1;
				if(count>to+100)
					to+=100;
				else to=count;
			
			} catch (JSONException e) {
				e.printStackTrace();
			}	
		}	
	}

	/**
	 * Main method
	 * creates a new PanoramioCrawler and starts it in an endless loop
	 * @throws IOException, NotBoundException
	 * */
	public static void main(String args[]) throws IOException, NotBoundException {
		ACrawler crawler = new PanoramioCrawler();
		crawler.crawl();
	}

}
