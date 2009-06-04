package at.fakeroot.sepm.crawler;
import java.rmi.Remote;
import java.rmi.RemoteException;

import at.fakeroot.sepm.shared.server.DBGeoObject;


public interface GeoSave extends Remote {
	public void saveObject(DBGeoObject[] newGeoObj)throws RemoteException;
	
	public int getServiceID(String svcName)throws RemoteException;
}
