package at.fakeroot.sepm.crawler.locr;
import java.io.IOException;
import java.rmi.NotBoundException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import at.fakeroot.sepm.crawler.ACrawler;
import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import at.fakeroot.sepm.shared.server.DBGeoObject;
import at.fakeroot.sepm.shared.server.Property;



public class LocrCrawler extends ACrawler {

	public LocrCrawler() throws IOException, NotBoundException {
		super("locr.com");
	}


	protected void crawlBox(BoundingBox curBox) {
		String url = "http://de.locr.com/api/get_photos_json.php"
			+ "?latitudemin=" + curBox.getX1() + 
			"&longitudemin=" + curBox.getY1() + 
			"&latitudemax=" + curBox.getX2()
			+ "&longitudemax=" + curBox.getY2()
			+ "&category=popularity" + "&locr=true";

	//System.out.println(curBox);
	System.out.println(url);
	//System.out.println(crawl(url));

	try {
		String[] tabs= {"tag1","tag2"};
	    Property[] myProperty = new Property[1];
	    myProperty[0]= new Property("proName","proValue");
	    
	    
		JSONObject myJSONObject = new JSONObject(requestUrl(url));
		//System.out.println("Parse: "+myJSONObject.getJSONArray("photos"));
		JSONArray myArray = myJSONObject.getJSONArray("photos");
		DBGeoObject[] saveDBArray = new DBGeoObject[myArray.length()];
		for(int k=0;k<myArray.length();k++){
			System.out.println("id: "+(((JSONObject)myArray.get(k)).get("photo_id")));
			
			//Integer.parseInt((((JSONObject)myArray.get(k)).get("photo_id").toString()));
			saveDBArray[k]=new DBGeoObject(
					Integer.parseInt((((JSONObject)myArray.get(k)).get("photo_id").toString())),
					"Client1",
					Double.parseDouble((((JSONObject)myArray.get(k)).get("latitude").toString())),
					Double.parseDouble((((JSONObject)myArray.get(k)).get("longitude").toString())),
					getSvcID(),
					"uniqu",
					"link",
					null,
					myProperty,tabs);
			
		}
		
		saveObject(saveDBArray);
		
	} catch (JSONException e) {
		e.printStackTrace();
	}
		
	}
}
