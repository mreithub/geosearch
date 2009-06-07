package at.fakeroot.sepm.shared.server;
import java.rmi.Remote;
import java.rmi.RemoteException;

import at.fakeroot.sepm.shared.server.DBGeoObject;


public interface IGeoSave extends Remote {
	public void saveObject(DBGeoObject geoObj)throws RemoteException;
	
	public void saveObjects(DBGeoObject objects[]) throws RemoteException;
	
	public int getServiceID(String svcName)throws RemoteException;
}
