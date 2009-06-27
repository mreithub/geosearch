package at.fakeroot.sepm.crawler;

import java.rmi.RemoteException;

import at.fakeroot.sepm.shared.server.DBGeoObject;
import at.fakeroot.sepm.shared.server.IGeoSave;

public class GeoSaveTest implements IGeoSave {

	@Override
	public int getServiceID(String svcName) throws RemoteException {
		return 23;
	}

	@Override
	public void saveObject(DBGeoObject geoObj) throws RemoteException {
		System.out.println("GeoSave.saveObject: "+geoObj);
	}

	@Override
	public void saveObjects(DBGeoObject[] objects) throws RemoteException {
		System.err.println("GeoSave.saveObjects ("+objects.length+")");
		for (int i = 0; i < objects.length; i++) {
			saveObject(objects[i]);
		}
	}

	@Override
	public String getSplitChars() throws RemoteException {
		return null;
	}

	@Override
	public String[] getStopWords() throws RemoteException {
		return new String[]{};
	}

}
