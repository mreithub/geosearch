package at.fakeroot.sepm.server;

import java.rmi.RemoteException;
import java.sql.SQLException;

import at.fakeroot.sepm.server.GeoObjectManager.NotFoundException;
import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import at.fakeroot.sepm.shared.client.serialize.SearchResult;
import at.fakeroot.sepm.shared.server.DBGeoObject;

public interface IGeoObjectManager {

	/**
	 * Method used to get the obj_id of the DBGeoObject with the given svc_id and uid
	 * @param svc_id the service id 
	 * @param uid the unique id 
	 * @return the obj_id 
	 * @throws NotFoundException if no object was found
	 * */
	public abstract long getObjectId(int svc_id, String uid)
			throws NotFoundException, SQLException;

	/**
	 * Get the DBGeoObject with this obj_id
	 * @param obj_id the object id 
	 * @return  the DBGeoObject with this id
	 * @throws Exception if no object with this id is found
	 * */
	public abstract DBGeoObject getObjectbyId(long id)
			throws NotFoundException, SQLException;

	/**
	 * Delete a specific geoObject from the database
	 * @param objId ID of the object to be deleted (can be queried by getObjectId() or by GeoObject.getId()
	 * @throws NotFoundException when the object to delete doesn't exist
	 * @throws SQLException on any other database related error
	 */
	public abstract void delete(long objId) throws NotFoundException,
			SQLException;

	/**
	 * Delete a specific geoObject via it's service ID and it's UID
	 * @param svcId service ID for the geoObject
	 * @param uid service-unique ID of the geoObject 
	 * @throws NotFoundException when no matching geoObject is found in the database
	 * @throws SQLException on any other database related problem
	 */
	public abstract void delete(int svcId, String uid)
			throws NotFoundException, SQLException;

	/**
	 * Method used to retrieve a limited number of ClientGeoObjects having a set of tags and lying in a particular BoundingBox
	 * @param tags String[] the desired tags
	 * @param box BoundingBox the search area
	 * @param displayLimit int the number of results to fetch
	 * @param countLimit int the number of results to count
	 * */
	public abstract SearchResult select(String[] tags, BoundingBox box,
			int displayLimit, int countLimit);

	/**
	 * Insert a new object in the Database
	 * @param obj DBGeoObject object to be inserted 
	 * */
	public abstract void insert(DBGeoObject obj) throws RemoteException;

	/**
	 * Update an object in the database
	 * @param the new object, that will overwrite the old one with the same object id (obj_id)
	 * */
	public abstract void update(DBGeoObject obj) throws RemoteException;

}