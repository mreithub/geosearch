package at.fakeroot.sepm.server;

import java.rmi.RemoteException;

import at.fakeroot.sepm.shared.server.DBGeoObject;
import at.fakeroot.sepm.shared.server.DBService;
import at.fakeroot.sepm.shared.server.GeoObjectManager;
import at.fakeroot.sepm.shared.server.IGeoSave;
import at.fakeroot.sepm.shared.server.ServiceManager;


public class GeoSave implements IGeoSave
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
	
	public void saveObjects(DBGeoObject objects[]) {
		for (int i = 0; i < objects.length; i++) {
			saveObject(objects[i]);
		}
	}

	@Override
	public int getServiceID(String svcName) throws RemoteException {
		ServiceManager serviceManager = ServiceManager.getInstance();
		
		DBService svc = serviceManager.select(svcName);
		
		return svc.getSvc_id();
	}
}