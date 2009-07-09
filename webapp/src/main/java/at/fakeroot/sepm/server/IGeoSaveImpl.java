package at.fakeroot.sepm.server;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import at.fakeroot.sepm.shared.server.DBGeoObject;
import at.fakeroot.sepm.shared.server.IGeoSave;
import at.fakeroot.sepm.server.GeoObjectManager.NotFoundException;

/**
 * Implements the Crawler RMI (RPC) Connections
 * @author JB
 *
 */
public class IGeoSaveImpl implements IGeoSave {
	
	private static final Logger logger = Logger.getRootLogger();
	private IGeoObjectManager geoManager;
	
	/**
	 * constructer 
	 * @throws IOException
	 * @throws SQLException 
	 */
	public IGeoSaveImpl() {
		try {
			geoManager = GeoObjectManager.getInstance();
		}
		catch (SQLException e) {
			logger.error("SQLException", e);
		}
		catch (IOException e) {
			logger.error("IOException", e);
		}
	}
	
	/**
	 * Insert or update a GeoObject in the DB
	 * @param newGeoObj 
	 */
	public void saveObject(DBGeoObject obj) throws RemoteException {
		//Check if this object already exists. If yes, we have to update it.
		//If no, we have to insert it. Note that the passed DBGeoObject only provides
		//the service ID and the service object ID, but not the object ID itself.
		
		//There mustn't exist any object ID within this object.
		obj.setId(0);
		long obj_id=0;
		
		try{
			obj_id = geoManager.getObjectId(obj.getSvc_id(), obj.getUid());
			obj.setId(obj_id);
			geoManager.update(obj);
		} catch(NotFoundException e) {
			geoManager.insert(obj);
		} catch(SQLException e) {
			logger.error("SQL exception in IGeoSaveImpl.saveObject()", e);
			throw new RemoteException("SQL exception in IGeoSaveImpl.saveObject()", e);
		} catch(IOException e) {
			logger.error("IO exception in IGeoSaveImpl.saveObject()", e);
			throw new RemoteException("IO exception in IGeoSaveImpl.saveObject()", e);
		}
	}

	/**
	 * Return the serviceID of an given serviceName
	 * @param svcName serviceName (eg. panoramio.com)
	 */
	public int getServiceID(String svcName) throws RemoteException {
		IServiceManager serviceManager = ServiceManager.getInstance();
		DBService svc;
		
		try {
			svc = serviceManager.select(svcName);
		}
		catch (SQLException e) {
			throw new RemoteException("IGeoSaveImpl.getServiceID failed", e);
		}
		catch (IOException e) {
			throw new RemoteException("IGeoSaveImpl.getServiceID failed", e);
		}
		
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

	
	/**
	 * 
	 * @return Returns the StopWords (German)
	 * @throws RemoteException
	 */
	public String[] getStopWords() throws RemoteException{
		SplitStopWordManager swManager = SplitStopWordManager.getInstance();
		String[] rc;
		try {
			rc = swManager.getStopWords();
		} catch (SQLException e) {
			throw new RemoteException("IGeoSaveImpl.getStopWords failed", e);
		} catch (IOException e) {
			throw new RemoteException("IGeoSaveImpl.getStopWords failed", e);
		}
		
		return rc;		
	}
	
	/**
	 * 
	 * @return Returns SplitCharacters
	 * @throws RemoteException
	 */
	public String getSplitChars() throws RemoteException{
		SplitStopWordManager swManager = SplitStopWordManager.getInstance();
		String rc;
		
		try {
			rc = swManager.getSplitChars();
		} catch (SQLException e) {
			throw new RemoteException("IGeoSaveImpl.getSplitChars failed", e);
		} catch (IOException e) {
			throw new RemoteException("IGeoSaveImpl.getSplitChars failed", e);
		}
		
		return rc;
	}

}
