package at.fakeroot.sepm.crawler;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import at.fakeroot.sepm.shared.server.DBGeoObject;
import at.fakeroot.sepm.shared.server.Property;

public class LastFMCrawler extends ACrawler
{
	/**
	 * Standard constructor.
	 */
	public LastFMCrawler()
	{
		//From last.fm:
		//Your API Key is ca211b536eeba529327dcb4d0a7ca6b6 and your secret is 48fa6e4fc3702d4d188eda2e6cabd7d9		
		
		//0.32 degree are about 70 km.
		super(ACrawler.AUSTRIA, 0.32, "lastfm");
	}
	
	/**
	 * This function starts the crawling process and runs it. Note that this function doesn't terminate,
	 * it must therefore execute within a separate thread.
	 */
	public void startCrawling()
	{
		//Create the date formatter (needed for parsing the date strings) and the result geo object list.
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.US);
		ArrayList<DBGeoObject> geoObjects = new ArrayList<DBGeoObject>();
		
		//TODO diese Schleife entsprechend anpassen. (Endlos-Schleife)
		for (int dummy = 0; dummy < 1; dummy++)
		{
			//curPage is the current page of the response, numPages contains the total amount of response pages.
			int curPage = 1, numPages = 0;
			String responseStr;
			BoundingBox curBox = nextCrawlBox();
			
			//Create the request string.
			String url = "http://ws.audioscrobbler.com/2.0/?method=geo.getevents" +
				"&lat=" + (curBox.getY1() + curBox.getY2()) / 2 +
				"&long=" + (curBox.getX1() + curBox.getX2()) / 2 +
				"&distance=50" +
				"&format=json" +
				"&api_key=ca211b536eeba529327dcb4d0a7ca6b6";

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
							String titel = null, uniqueID = null, link = null;
							ArrayList<String> tags = null;
							double xPos = 0, yPos = 0;
							Timestamp validUntil = null;
							boolean foundData = false;
							
							//Get the current event.
							JSONObject curEvent = eventArray.getJSONObject(i);
							
							//Get the title.
							if (curEvent.isNull("title"))
								continue;
							titel = curEvent.getString("title");
							
							//Get the geo position.
							foundData = false;
							if (!curEvent.isNull("venue"))
							{
								JSONObject curVenue = curEvent.getJSONObject("venue");
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
							
							//Extract the event tags. We use the words from the title, artists, and description.
							tags = new ArrayList<String>();
							parseStringIntoTags(titel, tags, false);
							if (!curEvent.isNull("artists"))
							{
								JSONObject curArtists = curEvent.getJSONObject("artists");
								if (!curArtists.isNull("artist"))
								{
									JSONArray curArtistArray = curArtists.optJSONArray("artist");
									if (curArtistArray != null)
									{
										for (int j = 0; j < curArtistArray.length(); j++)
											parseStringIntoTags(curArtistArray.getString(j), tags, false);
									}
									else
										parseStringIntoTags(curArtists.getString("artist"), tags, false);
								}
							}
							if (!curEvent.isNull("description"))
							{
								String desc = curEvent.getString("description");
								parseStringIntoTags(desc, tags, true);
							}						
							
							//Add a new DB geo object with the parsed values.
							geoObjects.add(new DBGeoObject(0, titel, xPos, yPos, getSvcID(), uniqueID,
								link, validUntil, new Property[] {}, tags.toArray(new String[tags.size()])));
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
}
