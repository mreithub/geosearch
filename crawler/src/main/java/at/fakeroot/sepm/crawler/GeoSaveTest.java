package at.fakeroot.sepm.crawler;

import java.rmi.RemoteException;

import at.fakeroot.sepm.shared.server.DBGeoObject;
import at.fakeroot.sepm.shared.server.IGeoSave;

/**
 * IGeoSave test implementation to see if a crawler works fine
 * 
 * This implementation simply prints the DBGeoObjects the crawler wants to save 
 *  
 * @author Manuel Reithuber
 */
public class GeoSaveTest implements IGeoSave {
	/**
	 * return a fake service ID
	 * @return fake service ID
	 */
	@Override
	public int getServiceID(String svcName) {
		return 23;
	}

	/**
	 * Pretend to save a single DBGeoObject (instead print the object's data)
	 * @param geoObj DBGeoObject to save
	 */
	@Override
	public void saveObject(DBGeoObject geoObj) {
		System.out.println("GeoSave.saveObject: "+geoObj);
	}

	/**
	 * Save several DBGeoObjects (call saveObject() for each of them)
	 * @param objects DBGeoObjects to save
	 */
	@Override
	public void saveObjects(DBGeoObject[] objects) {
		System.err.println("GeoSave.saveObjects ("+objects.length+")");
		for (int i = 0; i < objects.length; i++) {
			saveObject(objects[i]);
		}
	}

	/**
	 * return some data that could actually be in the splitChars-table
	 * @return String filled with the split characters
	 */
	@Override
	public String getSplitChars() {
		return " \r\n.,!?\"`´°\\\'";
	}

	/**
	 * return some sample stop words
	 * @return String array with stop words
	 */
	@Override
	public String[] getStopWords() {
		return new String[]{"der", "die", "das", "in", "ein", "aus", "für"};
	}

}
