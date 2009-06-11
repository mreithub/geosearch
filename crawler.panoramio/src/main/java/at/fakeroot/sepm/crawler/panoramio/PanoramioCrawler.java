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

public class PanoramioCrawler extends ACrawler {

	public PanoramioCrawler() throws IOException, NotBoundException {
		super("panoramio.com");
	}
		

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
				from=to;
				if(count<to+99)
					to+=99;
				else to=count;
			
			} catch (JSONException e) {
				e.printStackTrace();
			}	
		}	
	}

	
	public static void main(String args[]) throws IOException, NotBoundException {
		new PanoramioCrawler();
	}

}
