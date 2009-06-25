package at.fakeroot.sepm.shared.server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import at.fakeroot.sepm.shared.client.serialize.ClientGeoObject;
import at.fakeroot.sepm.shared.client.serialize.SearchResult;

/**
 * Class representing the DAO for the GeoObjects, that reads from and writes to the database
 * Based on the Singleton pattern.
 * @author Anca Cismasiu
 */
public class GeoObjectManager 
{
	private static GeoObjectManager geoObjManager = null;
	private static final Logger logger = Logger.getRootLogger();
	DBConnection dbRead, dbWrite;

	public class NotFoundException extends Exception {
		/// default serial version id
		private static final long serialVersionUID = 1L;

		public NotFoundException() {
			super();
		}

		public NotFoundException(String message) {
			super(message);
		}

		public NotFoundException(String message, Throwable cause) {
			super(message, cause);
		}
	}


	/**
	 * Constructor that establishes the DB Connection
	 * @throws IOException if the database configuration could not be read
	 * @throws SQLException if the database connection could not be made
	 */
	private GeoObjectManager() throws SQLException, IOException
	{
		dbRead = new DBConnection();
		dbWrite = new DBConnection();
		dbWrite.setAutoCommit(false);
	}

	protected void finalize() throws SQLException {
		dbRead.disconnect();
		dbWrite.setAutoCommit(true);
		dbWrite.disconnect();
	}

	/**
	 * Method used for obtaining an instance of this Singleton class
	 * @throws IOException if the database configuration could not be read
	 * @throws SQLException if the database connection could not be made
	 */	
	public static GeoObjectManager getInstance() throws SQLException, IOException
	{
		if(geoObjManager == null)
			geoObjManager = new GeoObjectManager();
		return geoObjManager;
	}


	/**
	 * Method used to get the obj_id of the DBGeoObject with the given svc_id and uid
	 * @param svc_id the service id 
	 * @param uid the unique id 
	 * @return the obj_id 
	 * @throws Exception if no or more than one objects are found
	 * */	
	public long getObjectId(int svc_id, String uid) throws NotFoundException, SQLException, IOException {
		long rc=-1;
		try{
			PreparedStatement pstmt = dbRead.prepareStatement("SELECT obj_id FROM geoObject WHERE svc_id=? AND uid=?;");
			pstmt.setInt(1, svc_id);
			pstmt.setString(2, uid);
			ResultSet result= pstmt.executeQuery();

			if(result.next()) {	
				rc=result.getLong("obj_id");
			}
			else {
				throw new NotFoundException("svc_id: "+svc_id+", uid: "+uid);
			}
		} catch(SQLException e) {
			logger.error("SQLException in GeoObjectManager.getObjectId(int svc_id, String uid)", e);
			throw e;
		}

		return rc;
	}


	/**
	 * Get the DBGeoObject with this obj_id
	 * @param obj_id the object id 
	 * @return  the DBGeoObject with this id
	 * @throws Exception if no object with this id is found
	 * */
	public DBGeoObject getObjectbyId(long id) throws NotFoundException, SQLException, IOException {
		DBGeoObject rc=null;

		try{
			//long id, String title, double xPos, double yPos, int serviceID, String uniqueID, String link, Timestamp valid_until, Property[] properties, String[] tags){
			PreparedStatement pstmt = dbRead.prepareStatement("SELECT title, lng, lat, svc_id, uid, link, valid_until "
					+ "FROM geoObject LEFT JOIN expiringObject e USING (obj_id) WHERE obj_id = ?");
			pstmt.setLong(1, id);
			ResultSet res = pstmt.executeQuery();

			if(res.next()){
				res.first();
				rc = new DBGeoObject(
						id,
						res.getString("title"),
						res.getDouble("lng"), res.getDouble("lat"),
						res.getInt("svc_id"),
						res.getString("uid"),
						res.getString("link"),
						res.getTimestamp("valid_until"),
						getProperties(id),
						queryTags(id));
			}
			else {
				throw new NotFoundException("obj_id: "+id);
			}
		}catch(SQLException e){
			logger.error("SQLException in GeoObjectManager.getObjectById(long id)", e);
			throw e;
		}

		return rc;
	}

	/**
	 * Method used to retrieve a limited number of ClientGeoObjects having a set of tags and lying in a particular BoundingBox
	 * @param tags String[] the desired tags
	 * @param box BoundingBox the search area
	 * @param displayLimit int the number of results to fetch
	 * @param countLimit int the number of results to count
	 * */
	public SearchResult select(String[] tags, BoundingBox box, int displayLimit, int countLimit)
	{
		SearchResult searchResult = new SearchResult(countLimit);
		PreparedStatement pstmt;
		ResultSet res;

		try {
			String joinStmt = "LEFT JOIN expiringObject e USING (obj_id) ";
			
			//Get the result count
			String sql = "SELECT COUNT(subquery.obj_id) FROM (SELECT obj_id FROM geoObject " + joinStmt +
				getWhereClause(box, tags.length, false, countLimit) + ") AS subquery";
			pstmt = dbRead.prepareStatement(sql);
			for (int i = 0; i < tags.length; i++)
			{
				pstmt.setString(i * 2 + 1, tags[i]);
				pstmt.setString(i * 2 + 2, tags[i]);
			}
			res = pstmt.executeQuery();
			res.next();
			searchResult.setResultCount(res.getInt(1));
			res.close();
			pstmt.close();
	
			
			//Get the result set which contains the selected geoObjects.
			sql = "SELECT obj_id, svc_id, o.title, lng, lat, t.thumbnail FROM geoObject o " +
				"INNER JOIN service USING (svc_id) INNER JOIN serviceType t USING (stype_id) " +
				joinStmt + getWhereClause(box, tags.length, true, displayLimit);
			pstmt = dbRead.prepareStatement(sql);
			for (int i = 0; i < tags.length; i++)
			{
				pstmt.setString(i * 2 + 1, tags[i]);
				pstmt.setString(i * 2 + 2, tags[i]);
			}
			res = pstmt.executeQuery();
			
			//Get the object tags for all those selected objects. Use a single query for that.
			while (res.next())
			{
				//Create the objects with empty tags here. (We can only loop through the result set
				//once, therefore we have to set the tags later.
				searchResult.addResultToList(new ClientGeoObject(
					res.getLong("obj_id"),
					res.getString("title"),
					res.getString("thumbnail"),
					null,
					res.getDouble("lng"),
					res.getDouble("lat")));
			}
			res.close();
			pstmt.close();
			
			if (searchResult.getResults().size() > 0)
			{
				//Fill in the tags from the database into the ClientGeoObjects.
				queryTags(searchResult);
			}
		}
		catch (SQLException e) {
			logger.error("SQL error in GeoObjectManager.select()", e);
			searchResult.setErrorMessage(e.getMessage());
		}

		return searchResult;
	}

	private String getWhereClause(BoundingBox box, int numberTags, boolean randomOrder, int limit) throws SQLException {
		String sql = "WHERE (e.valid_until IS null OR e.valid_until >= now()) AND ";
		
		//Create the WHERE clause for the geographical location.
		sql += "(lng BETWEEN " + box.getX1() + " AND " + box.getX2() + ") AND (lat BETWEEN " + box.getY1() + " AND " + box.getY2() + ") AND ";
		
		//Create a where clause for each passed tag.
		if (numberTags > 0)
		{
			for (int i = 0; i < numberTags; i++)
			{
				sql += "(obj_id IN (SELECT obj_id FROM objecttag WHERE tag = ?) " +
					"OR svc_id IN (SELECT svc_id FROM servicetag WHERE tag = ?)) AND ";
			}
		}

		//Remove the last " AND " from the query string again.
		sql = sql.substring(0, sql.length() - 5);
		
		if (randomOrder)
			sql += " ORDER BY rndVal DESC";
		if (limit > 0)
			sql += " LIMIT " + limit; 
				
		return (sql);
	}

	/**
	 * Fills the geoObjects with tags.
	 * @param result The search result, containing the objects to fill.
	 * @throws SQLException
	 */
	private void queryTags(SearchResult result) throws SQLException
	{
		ArrayList<ClientGeoObject> geoObjects = result.getResults();
		ArrayList<String> curObjTags = new ArrayList<String>();
		Iterator<ClientGeoObject> iter;
		
		//Set up the SQL query string.
		String sql = "SELECT obj_id, tag FROM objectTag WHERE obj_id IN (";
		iter = geoObjects.iterator(); 
		while (iter.hasNext())
			sql += iter.next().getId() + ", ";
		sql = sql.substring(0, sql.length() - 2) + ") ORDER BY obj_id";
		
		//Execute the query.
		PreparedStatement tagStmt = dbRead.prepareStatement(sql);
		ResultSet tagRes = tagStmt.executeQuery();
		long lastID = -1, curID;
		while (tagRes.next())
		{
			curID = tagRes.getLong("obj_id");
			if (curID != lastID)
			{
				if (lastID != -1)
				{
					//Get the index of the object with the lastID within the array.
					iter = geoObjects.iterator();
					while (iter.hasNext())
					{
						ClientGeoObject loopObj = iter.next();
						if (loopObj.getId() == lastID)
						{
							loopObj.setTags(curObjTags.toArray(new String[curObjTags.size()]));
							break;
						}
					}
				}
				curObjTags.clear();
				lastID = curID;
			}
			curObjTags.add(tagRes.getString("tag"));
		}
		
		//Set the tags of the last object too.
		//Get the index of the object with the lastID within the array.
		iter = geoObjects.iterator();
		while (iter.hasNext())
		{
			ClientGeoObject loopObj = iter.next();
			if (loopObj.getId() == lastID)
			{
				loopObj.setTags(curObjTags.toArray(new String[curObjTags.size()]));
				break;
			}
		}
		
		tagRes.close();
		tagStmt.close();
		
		//Check if there are objects where we didn't find any tags.
		iter = geoObjects.iterator();
		while (iter.hasNext())
		{
			ClientGeoObject loopObj = iter.next();
			if (loopObj.getTags() == null)
				loopObj.setTags(new String[0]);
		}
	}
	
	private String[] queryTags(long objId) throws SQLException {
		PreparedStatement tagStmt = dbRead.prepareStatement("SELECT tag FROM objectTag WHERE obj_id = ?");
		String[] rc;

		tagStmt.setLong(1, objId);
		ResultSet tagRes = tagStmt.executeQuery();
		tagRes.last();
		rc = new String[tagRes.getRow()];
		tagRes.beforeFirst();

		while (tagRes.next()) {
			rc[tagRes.getRow()-1] = tagRes.getString(1);
		}
		tagStmt.close();

		return rc;
	}	

	/**
	 * Get the properties for a GeoObject
	 * @param objId Object ID
	 * @return Property Array
	 * @throws SQLException
	 */
	private Property[] getProperties(long obj_id) throws SQLException {
		Property[] rc;
		PreparedStatement pstmt = dbRead.prepareStatement("SELECT name, value FROM objectProperty WHERE obj_id = ?"); 
		pstmt.setLong(1, obj_id);
		ResultSet rs= pstmt.executeQuery();
		rs.last();
		rc=new Property[rs.getRow()];
		rs.beforeFirst();
		while(rs.next()) {
			rc[rs.getRow()-1] = new Property(rs.getString("name"),rs.getString("value"));
		}
		pstmt.close();

		return rc;	
	} 


	/**
	 * Insert a new object in the Database
	 * @param obj DBGeoObject object to be inserted 
	 * */
	public void insert (DBGeoObject obj) throws RemoteException
	{
		try {
			PreparedStatement pstmt = dbWrite.prepareStatement("INSERT INTO geoObject(svc_id, uid, title, link, lng, lat) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, obj.getSvc_id());
			pstmt.setString(2, obj.getUid());
			pstmt.setString(3, obj.getTitle());
			pstmt.setString(4, obj.getLink());
			pstmt.setDouble(5, obj.getXPos());
			pstmt.setDouble(6, obj.getYPos());
			pstmt.executeUpdate();
			pstmt.close();
			
			// filter double tags
			String tags[] = obj.getTags();
			TreeSet<String> tagSet = new TreeSet<String>();
			for (int i = 0; i < tags.length; i++) {
				tagSet.add(tags[i].toLowerCase());
			}

			//insert new tags
			pstmt = dbWrite.prepareStatement("INSERT INTO objectTag(obj_id, tag) VALUES (currval('geoobject_obj_id_seq'), ?)");
			Iterator<String> it = tagSet.iterator();
			while(it.hasNext()) {
				pstmt.setString(1, it.next());
				pstmt.executeUpdate();
			}
			pstmt.close();

			//save the properties
			pstmt=dbWrite.prepareStatement("INSERT INTO objectProperty (obj_id, name, value) VALUES (currval('geoobject_obj_id_seq'), ?, ?)");
			Property[] prop = obj.getProperties();
			for(int i=0; i<prop.length; i++) {
				pstmt.setString(1, prop[i].getName());
				pstmt.setString(2, prop[i].getValue());
				pstmt.executeUpdate();
			}
			pstmt.close();

			// expiring objects
			Timestamp valid_until = obj.getValid_until();
			if (valid_until != null) {
				pstmt = dbWrite.prepareStatement("INSERT INTO expiringObject (obj_id, valid_until) VALUES (currval('geoobject_obj_id_seq'),?)");
				pstmt.setTimestamp(1, valid_until);
				pstmt.close();
			}
			dbWrite.commit();
		}
		catch (SQLException e) {
			try {
				dbWrite.rollback();
			}
			catch (SQLException ex) {}
			logger.error("SQL error in GeoObjectManager.insert", e);
			throw new RemoteException("SQL error in geoObjectManager.insert", e);
		}
	}

	/**
	 * Update an object in the database
	 * @param the new object, that will overwrite the old one with the same object id (obj_id)
	 * */
	public void update (DBGeoObject obj) throws RemoteException
	{
		try {
			PreparedStatement pstmt = dbWrite.prepareStatement("UPDATE geoObject SET title = ?, link = ?, lng = ?, lat = ?, last_updated = now() WHERE obj_id = ?");
			pstmt.setString(1, obj.getTitle());
			pstmt.setString(2, obj.getLink());
			pstmt.setDouble(3, obj.getXPos());
			pstmt.setDouble(4, obj.getYPos());
			pstmt.setLong(5, obj.getId());
			pstmt.executeUpdate();


			
			// filter double tags
			String tags[] = obj.getTags();
			TreeSet<String> tagSet = new TreeSet<String>();
			for (int i = 0; i < tags.length; i++) {
				tagSet.add(tags[i].toLowerCase());
			}

			// overwrite tags
			deleteTags(obj.getId());
			pstmt = dbWrite.prepareStatement("INSERT INTO objectTag(obj_id, tag) VALUES (?, ?)");
			Iterator<String> it = tagSet.iterator();
			while(it.hasNext()){
				String tag = it.next();
				pstmt.setLong(1, obj.getId());
				pstmt.setString(2, tag);
				pstmt.executeUpdate();
			}
			pstmt.close();
			
			// overwrite properties
			deleteProperties(obj.getId());
			pstmt=dbWrite.prepareStatement("INSERT INTO objectProperty (obj_id, name, value) VALUES (?, ?, ?)");
			Property[] prop = obj.getProperties();
			for(int i=0; i<prop.length; i++){
				pstmt.setLong(1, obj.getId());
				pstmt.setString(2, prop[i].getName());
				pstmt.setString(3, prop[i].getValue());
				pstmt.executeUpdate();
			}
			pstmt.close();
			
			// overwrite expiringObject
			pstmt = dbWrite.prepareStatement("DELETE FROM expiringObject where obj_id = ?");
			pstmt.setLong(1, obj.getId());
			pstmt.executeUpdate();
			pstmt.close();
			
			Timestamp valid_until = obj.getValid_until();
			if (valid_until != null) {
				pstmt = dbWrite.prepareStatement("INSERT INTO expiringObject (obj_id, valid_until) VALUES (?,?)");
				pstmt.setLong(1, obj.getId());
				pstmt.setTimestamp(2, valid_until);
				pstmt.close();
			}
			dbWrite.commit();
		}
		catch (SQLException e) {
			try {
				dbWrite.rollback();
			}
			catch (SQLException ex) {}
			logger.error("Error in GeoObjectManager.update()", e);
			throw new RemoteException("Error in GeoObjectManager.update()", e);
		}
	}

	/**
	 * Deletes the tags when object is updated
	 * @param objId the id of the object to be updated 
	 * */
	private void deleteTags(long objId) throws SQLException{
		PreparedStatement pstmt=dbWrite.prepareStatement("DELETE FROM objectTag WHERE obj_id = ?");
		pstmt.setLong(1, objId);
		pstmt.executeUpdate();
	}

	
	/**
	 * Deletes the properties when object is updated
	 * @param objId the id of the object to be updated 
	 * */
	private void deleteProperties(long objId) throws SQLException{
		PreparedStatement pstmt=dbWrite.prepareStatement("DELETE FROM objectProperty WHERE obj_id = ?");
		pstmt.setLong(1, objId);
		pstmt.executeUpdate();
	}
}
