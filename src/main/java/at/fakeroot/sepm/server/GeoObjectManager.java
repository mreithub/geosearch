package at.fakeroot.sepm.server;

import at.fakeroot.sepm.client.serialize.BoundingBox;
import at.fakeroot.sepm.client.serialize.GeoObject;
import at.fakeroot.sepm.client.serialize.SearchResult;

/**
 * 
 * DUMMY KLASSE UM COMPILE FEHLER ZU VERHINDERN
 *
 */
public class GeoObjectManager 
{
	private static GeoObjectManager geoObjManager = null;
	private DBConnection dbConn;
	
	protected GeoObjectManager()
	{
		try
		{
			dbConn = new DBConnection();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static GeoObjectManager getInstance()
	{
		if(geoObjManager == null)
			geoObjManager = new GeoObjectManager();
		return geoObjManager;
	}
	
	public DBGeoObject select(GeoObject geoObject)
	{
		return null;
	}
	
	public SearchResult select(String[] tags, BoundingBox box, int limit)
	{
		return null;
	}
}
