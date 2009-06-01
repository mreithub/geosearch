package at.fakeroot.sepm.server;

import java.util.ArrayList;

import at.fakeroot.sepm.client.serialize.BoundingBox;
import at.fakeroot.sepm.client.serialize.GeoObject;
import at.fakeroot.sepm.client.serialize.ObjectDetails;
import at.fakeroot.sepm.client.serialize.ObjectSearchService;
import at.fakeroot.sepm.client.serialize.SearchResult;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ObjectSearchServiceImpl extends RemoteServiceServlet implements ObjectSearchService
{

	private static final int limit = 50;
	private GeoObjectManager geoObjManager;
	private ServiceManager svcManager;
	
	
	public ObjectSearchServiceImpl()
	{
		geoObjManager = GeoObjectManager.getInstance();
		svcManager = ServiceManager.getInstance();
	}
	
	public SearchResult search(BoundingBox box, String what)
	{
		String[] tags = what.toLowerCase().split(" ");
		return (geoObjManager.select(tags, box, limit)); 
	}
	
	public ObjectDetails getDetailHTML(int objId)
	{
		DBGeoObject dbGeoObj = geoObjManager.select(new GeoObject(objId, null, 0, 0));
		DBService dbSvcObj = svcManager.select(dbGeoObj.getSvc_id());
		String html = dbSvcObj.getBubbleHTML();
		int begin = 0;
		int end = 0;
		String result = null;
		ArrayList<String> tags = new ArrayList<String>();
		for(int i = 0; i < html.length(); i++)
		{
			if(html.charAt(i) == '%')
			{
				begin = i;
				boolean endFound = false;
				for(int j = i+1; j < html.length(); j++)
				{
					
					if(html.charAt(j) == '%')
					{
						end = j;
						endFound = true;
						break;
					}
				}
				if(!endFound)
				{
					break;
				}
				result = html.substring(0, begin);
				for(int x = 0; x < dbGeoObj.getProperties().length; x++)
				{
					if(html.substring(begin + 1, end).equals(dbGeoObj.getProperties()[x].getName()))
					{
						result += dbGeoObj.getProperties()[x].getValue();
						result += html.substring(end+1);	  
						html = result;
						break;
					}
				}
			}
		}
		for(int i = 0; i < dbGeoObj.getTags().length; i++)
		{
			tags.add(dbGeoObj.getTags()[i]);
		}
		for(int i = 0; i < dbSvcObj.getTags().length; i++)
		{
			tags.add(dbSvcObj.getTags()[i]); 
		}
		return (new ObjectDetails(result,tags.toArray(new String[tags.size()])));
	}
	
}
