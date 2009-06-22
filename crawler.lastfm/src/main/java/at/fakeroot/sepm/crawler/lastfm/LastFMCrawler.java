package at.fakeroot.sepm.crawler.lastfm;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gwt.maps.client.geom.LatLng;

import at.fakeroot.sepm.crawler.ACrawler;
import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import at.fakeroot.sepm.shared.server.DBGeoObject;
import at.fakeroot.sepm.shared.server.Property;

public class LastFMCrawler extends ACrawler
{
	//Create the date formatter (needed for parsing the date strings) and the result geo object list.
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.US);
	private ArrayList<DBGeoObject> geoObjects = new ArrayList<DBGeoObject>();

	/**
	 * Main function for running the crawler.
	 * @param main
	 * @throws IOException
	 * @throws NotBoundException
	 */
	public static void main(String main[]) throws IOException, NotBoundException {
		ACrawler crawler = new LastFMCrawler();
		crawler.crawl();
	}
		
	/**
	 * Standard constructor.
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public LastFMCrawler() throws IOException, NotBoundException
	{		
		//Initialize the crawler.
		super("last.fm");
	}

	
	/**
	 * This function starts the crawling process and runs it. Note that this function doesn't terminate,
	 * it must therefore execute within a separate thread.
	 */
	public void crawlBox(BoundingBox curBox) {
		//curPage is the current page of the response, numPages contains the total amount of response pages.
		int curPage = 1, numPages = 0;
		String responseStr;
		
		//Compute the radius which should be used for the query (in kilometers).
		double radius;
		//TODO: Problem: GWT scheint hier nicht verwendbar zu sein.
		//radius = curBox.getCenter().distanceFrom(LatLng.newInstance(curBox.getY1(), curBox.getX1())) / 1000;
		radius = 40;
		
		//Create the request string.		
		String url = "http://ws.audioscrobbler.com/2.0/?method=geo.getevents" +
			"&lat=" + (curBox.getY1() + curBox.getY2()) / 2 +
			"&long=" + (curBox.getX1() + curBox.getX2()) / 2 +
			"&distance=" + radius +
			"&format=json" +
			"&api_key=" + getApiKey();

		//We have to process the response in multiple passes because there can be more than a single page
		//for a single request.
		geoObjects.clear();
		do
		{
			//Use page-level exception handling.
			try
			{
				//The first page is returned by default. If we want to receive a different page, we have
				//to specify this within the request string.
				if (curPage == 1)
					responseStr = requestUrl(url);
				else
					responseStr = requestUrl(url + "&page=" + curPage);
				
				//Create the JSON object to parse the response.
				JSONObject responseObj = new JSONObject(responseStr);
				if (responseObj.isNull("events"))
					break;
				responseObj = responseObj.getJSONObject("events");
				
				//Get the total number of pages if we run the first pass.
				if (curPage == 1)
				{
					if (responseObj.isNull("totalpages"))
						break;
					numPages = responseObj.getInt("totalpages");
				}
				
				//Loop through all events in the result.
				if (responseObj.isNull("event"))
					break;
				JSONArray eventArray = responseObj.getJSONArray("event");
				for (int i = 0; i < eventArray.length(); i++)
				{
					//Use event-level exception handling, too.
					try
					{
						String title = null, uniqueID = null, link = null;
						ArrayList<String> tags = null;
						ArrayList<Property> properties = new ArrayList<Property>();
						double xPos = 0, yPos = 0;
						Timestamp validUntil = null;
						boolean foundData = false;
						
						//Get the current event.
						JSONObject curEvent = eventArray.getJSONObject(i);
						
						//Get the title.
						if (curEvent.isNull("title"))
							continue;
						title = curEvent.getString("title");
						
						//Get the geo position.
						foundData = false;
						if (!curEvent.isNull("venue"))
						{
							JSONObject curVenue = curEvent.getJSONObject("venue");
							if (!curVenue.isNull("name"))
								properties.add(new Property("location", curVenue.getString("name")));
							if (!curVenue.isNull("location"))
							{
								JSONObject curLocation = curVenue.getJSONObject("location");
								if (!curLocation.isNull("geo:point"))
								{
									JSONObject curGeoPoint = curLocation.getJSONObject("geo:point");
									xPos = curGeoPoint.getDouble("geo:long");
									yPos = curGeoPoint.getDouble("geo:lat");
									foundData = true;
								}
							}
						}
						if (!foundData)
							continue;
						
						//Get the unique ID.
						if (curEvent.isNull("id"))
							continue;
						uniqueID = curEvent.getString("id");
						
						//Get the link of the entry: check if there is a website available. If not, use the last.fm link.
						foundData = false;
						if (!curEvent.isNull("website"))
						{
							link = curEvent.getString("website");
							if (link.length() > 0)
								foundData = true;
						}
						if (!foundData)
						{
							if (!curEvent.isNull("url"))
							{
								link = curEvent.getString("url");
								if (link.length() > 0)
									foundData = true;
							}
						}
						if (!foundData)
							continue;
						
						//Get the valid-until date. We need to parse the strings for the date.
						if (curEvent.isNull("startDate"))
							continue;
						String startTime = curEvent.getString("startDate");
						if (startTime.length() >= 4 && startTime.substring(3, 4).equals(","))
						{
							//Remove the leading day of the week.
							startTime = startTime.substring(4).trim();
						}
						if (!curEvent.isNull("startTime"))
						{
							startTime += " " + curEvent.getString("startTime");
							dateFormatter.applyPattern("dd MMM yyyy HH:mm");
						}
						else
							dateFormatter.applyPattern("dd MMM yyyy");
						
						validUntil = new Timestamp(dateFormatter.parse(startTime).getTime());
						properties.add(new Property("begin", startTime));
						
						//Extract the event tags. We use the words from the title, artists, and description.
						tags = new ArrayList<String>();
						parseStringIntoTags(title, tags, false);
						if (!curEvent.isNull("artists"))
						{
							JSONObject curArtists = curEvent.getJSONObject("artists");
							if (!curArtists.isNull("artist"))
							{
								String artistString = "";
								JSONArray curArtistArray = curArtists.optJSONArray("artist");
								if (curArtistArray != null)
								{
									for (int j = 0; j < curArtistArray.length(); j++)
									{
										String curArtist = curArtistArray.getString(j);
										artistString += curArtist + ", ";
										parseStringIntoTags(curArtist, tags, false);
									}
									artistString = artistString.substring(0, artistString.length() - 2);
								}
								else
								{
									artistString = curArtists.getString("artist");
									parseStringIntoTags(artistString, tags, false);
								}
								properties.add(new Property("artists", artistString));
							}
						}
						if (!curEvent.isNull("description"))
						{
							String desc = curEvent.getString("description");
							properties.add(new Property("description", desc));
							parseStringIntoTags(desc, tags, true);
						}
						
						//Add a new DB geo object with the parsed values.
						geoObjects.add(new DBGeoObject(0, title, xPos, yPos, getSvcID(), uniqueID, link, validUntil,
							properties.toArray(new Property[properties.size()]),
							tags.toArray(new String[tags.size()])));
					}
					catch (Exception e)
					{
						//If there was an error while parsing an event, skip over this event, but don't skip
						//the parsing of the complete page.
						e.printStackTrace();
					}
				}
			}
			catch (Exception e)
			{
				//If there was an error while parsing the page, skip over the whole page.
				e.printStackTrace();
			}
			
			//Save the parsed DB geo objects by using the provided save function from the ACrawler.
			saveObject(geoObjects.toArray(new DBGeoObject[geoObjects.size()]));
			
			curPage++;
		} while (curPage <= numPages);
		
	}
}
