package at.fakeroot.sepm.server;
import java.rmi.RemoteException;
import at.fakeroot.sepm.shared.server.DBGeoObject;
import at.fakeroot.sepm.shared.server.DBService;
import at.fakeroot.sepm.shared.server.GeoObjectManager;
import at.fakeroot.sepm.shared.server.IGeoSave;
import at.fakeroot.sepm.shared.server.ServiceManager;

/**
 * Implements the Crawler RMI (RPC) Connections
 * @author JB
 *
 */
public class IGeoSaveImpl implements IGeoSave {
	
	/**
	 * Insert or update a GeoObject in the DB
	 * @param newGeoObj 
	 */
	public void saveObject(DBGeoObject newGeoObj) {
		//Check if this object already exists. If yes, we have to update it.
		//If no, we have to insert it. Note that the passed DBGeoObject only provides
		//the service ID and the service object ID, but not the object ID itself.
		
		//There mustn't exist any object ID within this object.
		newGeoObj.setId(0);
		
		DBGeoObject selectObj = GeoObjectManager.getInstance().select(newGeoObj);
		if (selectObj == null)
			GeoObjectManager.getInstance().insert(newGeoObj);
		else
		{
			newGeoObj.setId(selectObj.getId());
			GeoObjectManager.getInstance().update(newGeoObj);
		}
		
		
	}

	/**
	 * Return the serviceID of an given serviceName
	 * @param svcName serviceName (eg. panoramio.com)
	 */
	public int getServiceID(String svcName) throws RemoteException {
		ServiceManager serviceManager = ServiceManager.getInstance();
		
		DBService svc = serviceManager.select(svcName);
		
		return svc.getSvc_id();
	}

	/**
	 * Insert or update a array of GeoObjects in the DB
	 * @param objects 
	 */
	public void saveObjects(DBGeoObject[] objects) throws RemoteException {
		for (int i = 0; i < objects.length; i++) {
			saveObject(objects[i]);
		}
	}


}
