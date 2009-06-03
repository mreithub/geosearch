package at.fakeroot.sepm.geoSearch.shared;


public class GeoSave
{
	private static GeoSave instance;
	
	private GeoSave()
	{
	}
	
	public static GeoSave getInstance()
	{
		if (instance == null)
			instance = new GeoSave();
		return(instance);
	}
	
	public void saveObject(DBGeoObject obj)
	{
		//Check if this object already exists. If yes, we have to update it.
		//If no, we have to insert it. Note that the passed DBGeoObject only provides
		//the service ID and the service object ID, but not the object ID itself.
		
		//There mustn't exist any object ID within this object.
		obj.setId(0);
		
		DBGeoObject selectObj = GeoObjectManager.getInstance().select(obj);
		if (selectObj == null)
			GeoObjectManager.getInstance().insert(obj);
		else
		{
			obj.setId(selectObj.getId());
			GeoObjectManager.getInstance().update(obj);
		}
	}
}
