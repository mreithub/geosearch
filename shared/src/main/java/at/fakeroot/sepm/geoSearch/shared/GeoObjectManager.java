package at.fakeroot.sepm.geoSearch.shared;

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
	
	public DBGeoObject select(DBGeoObject obj)
	{
		//Anmerkung:
		//diese select-Funktion muss (zumindest) die Attribute obj_id, svc_id, uid beruecksichtigen
		//(siehe Aufruf der Funktion in der GeoSave-Klasse).
		return null;
	}
	
	public SearchResult select(String[] tags, BoundingBox box, int limit)
	{
		return null;
	}
	
	public void insert (DBGeoObject obj)
	{
	}
	
	public void update (DBGeoObject obj)
	{
		//Anmerkung: Identifikation des Objekts muss hier ueber das Attribut obj_id erfolgen
		//(siehe Aufruf der Funktion in der GeoSave-Klasse).
	}
}