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
 * @author AC
 * */

public class PanoramioCrawler extends ACrawler {

	private int stepSize=100;
	
	/**
	 * Main method
	 * creates a new PanoramioCrawler and starts it in an endless loop
	 * @throws IOException, NotBoundException
	 * */
	public static void main(String args[]) throws IOException, NotBoundException {
		PanoramioCrawler crawler = new PanoramioCrawler();
		crawler.crawl();
	}
	
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
	protected void crawlBox(BoundingBox curBox) {
		recCrawl(curBox, 0);
	}
	
	/**
	 * Recursive method, 
	 * */
	private void recCrawl(BoundingBox curBox, int start){
		String url="";
		String responseStr = "";
		

		url = "http://www.panoramio.com/map/get_panoramas.php?order=popularity&set=public" +
				"&from=" + start + "&to=" + (start+stepSize) +
				"&minx=" + curBox.getX1()+ "&miny=" + curBox.getY1()+ 
				"&maxx=" + curBox.getX2()+ "&maxy=" + curBox.getY2()+ "&size=small";
	
		responseStr = requestUrl(url);
		
		System.out.println(url);
	
		try {
			JSONObject jsonResponse = new JSONObject(responseStr);
			JSONArray photoArray = jsonResponse.getJSONArray("photos");
			System.err.println("photoArray: "+photoArray.length());
			DBGeoObject[] saveDBArray = new DBGeoObject[photoArray.length()];
			for(int i=0; i<photoArray.length(); i++){
				Property[] tmpProp = new Property[4];
				tmpProp[0]= new Property("owner", photoArray.getJSONObject(i).getString("owner_name"));
				tmpProp[1]= new Property("photo_url", photoArray.getJSONObject(i).getString("photo_file_url"));
				tmpProp[2] = new Property("height", photoArray.getJSONObject(i).getString("height"));
				tmpProp[3] = new Property("width", photoArray.getJSONObject(i).getString("width"));
				
				ArrayList<String> tags=new ArrayList<String>();
				parseStringIntoTags( photoArray.getJSONObject(i).getString("photo_title"), tags, true);
				
				DBGeoObject tmpObj = new DBGeoObject(
						photoArray.getJSONObject(i).getString("photo_title"), 
						photoArray.getJSONObject(i).getDouble("longitude"),
						photoArray.getJSONObject(i).getDouble("latitude"), 
						0, 
						JSONObject.numberToString(photoArray.getJSONObject(i).getInt("photo_id")),
						photoArray.getJSONObject(i).getString("photo_url"),
						null, 
						tmpProp,
						tags.toArray(new String[tags.size()]) );
				saveDBArray[i]=tmpObj;
				
			}
			
			saveObject(saveDBArray);
			
			
			if(photoArray.length()==stepSize){
				System.err.println("rec");
				recCrawl(curBox, start+stepSize);
			}else{
				System.err.println("noRec");
			}
			
				
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		
	}

	

}
