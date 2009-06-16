package at.fakeroot.sepm.crawler.locr;
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



public class LocrCrawler extends ACrawler {

	private int stepSize=12;
	
	public static void main(String main[]) throws IOException, NotBoundException{
		LocrCrawler test = new LocrCrawler();
		test.crawl();
	}
	
	public LocrCrawler() throws IOException, NotBoundException {
		super("locr.com");
	}


	protected void crawlBox(BoundingBox curBox) {
		recCrawl(curBox, 0);
	}
	
	private void recCrawl(BoundingBox curBox, int start){
		String url = "http://de.locr.com/api/get_photos_json.php"
			+ "?latitudemin=" + curBox.getX1()+ 
			"&longitudemin=" + curBox.getY1() + 
			"&latitudemax=" + curBox.getX2()
			+ "&longitudemax=" + curBox.getY2()
			+ "&category=popularity" 
			+ "&locr=true"
			+ "&start="+start
			+ "&count="+stepSize;

		//System.out.println(curBox);
		System.out.println(url);
		//System.out.println(crawl(url));
	
		try {
			String requestString=requestUrl(url);
			if(requestString!=null){
				ArrayList<String> tags;
			    Property[] myProperty = new Property[3];
			    
				JSONObject myJSONObject = new JSONObject(requestString);
				//System.out.println("Parse: "+myJSONObject.getJSONArray("photos"));
				JSONArray myArray = myJSONObject.getJSONArray("photos");
				
				int picArrLength=myArray.length();
				DBGeoObject[] saveDBArray = new DBGeoObject[picArrLength];
				System.out.println("arrayLength"+myArray.length());
				for(int k=0;k<myArray.length();k++){
					//pic url small: {picPath}{picName}_s.{picExt}
					//pic url medium: {picPath}{picName}_m.{picExt}
					myProperty[0]= new Property("picPath",(((JSONObject)myArray.get(k)).get("photo_path").toString()));
				    myProperty[1]= new Property("picName",(((JSONObject)myArray.get(k)).get("photo_name").toString()));
				    myProperty[2]= new Property("picExt",(((JSONObject)myArray.get(k)).get("photo_ext").toString()));
					
				    
				    //Tags
				    tags = new ArrayList<String>();
				    parseStringIntoTags((((JSONObject)myArray.get(k)).get("tags").toString()), tags, false);
				    parseStringIntoTags((((JSONObject)myArray.get(k)).get("location_name").toString()), tags, false);
				    
					//Integer.parseInt((((JSONObject)myArray.get(k)).get("photo_id").toString()));
					saveDBArray[k]=new DBGeoObject(
							Integer.parseInt((((JSONObject)myArray.get(k)).get("photo_id").toString())),
							(((JSONObject)myArray.get(k)).get("location_name").toString()),
							Double.parseDouble((((JSONObject)myArray.get(k)).get("latitude").toString())),
							Double.parseDouble((((JSONObject)myArray.get(k)).get("longitude").toString())),
							getSvcID(),
							(((JSONObject)myArray.get(k)).get("photo_id").toString()),
							(((JSONObject)myArray.get(k)).get("photo_url").toString()),
							null,
							myProperty,
							tags.toArray(new String[tags.size()]));
					
				}
				
				if(picArrLength>0){
					saveObject(saveDBArray);
				}
				
				if(myArray.length()==stepSize){
					System.out.println("rec");
					recCrawl(curBox, start+stepSize);
				}else{
					System.out.println("noRec");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
