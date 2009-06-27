package at.fakeroot.sepm.shared.server;
import java.rmi.Remote;
import java.rmi.RemoteException;

import at.fakeroot.sepm.shared.server.DBGeoObject;


public interface IGeoSave extends Remote {
	/**
	 * Insert or update a GeoObject in the DB
	 * @param newGeoObj 
	 */
	public void saveObject(DBGeoObject geoObj)throws RemoteException;
	
	/**
	 * Insert or update a array of GeoObjects in the DB
	 * @param objects 
	 */
	public void saveObjects(DBGeoObject objects[]) throws RemoteException;
	
	/**
	 * Return the serviceID of an given serviceName
	 * @param svcName serviceName (eg. panoramio.com)
	 */
	public int getServiceID(String svcName)throws RemoteException;
	
	
	/**
	 * 
	 * @return Returns the StopWords (German)
	 * @throws RemoteException
	 */
	public String[] getStopWords() throws RemoteException;
	
	/**
	 * 
	 * @return Returns SplitCharacters
	 * @throws RemoteException
	 */
	public String getSplitChars() throws RemoteException;
}
