package at.fakeroot.sepm.shared.server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.postgresql.geometric.PGpoint;
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
	DBConnection dbConn;

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
		dbConn = new DBConnection();
	}
	
	protected void finalize() throws SQLException {
		dbConn.disconnect();
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
			PreparedStatement pstmt = dbConn.prepareStatement("SELECT obj_id FROM geoObject WHERE svc_id=? AND uid=?;");
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
			PreparedStatement pstmt = dbConn.prepareStatement("SELECT title, pos[0] AS xPos, pos[1] AS yPos, svc_id, uid, link, valid_until "
					+ "FROM geoObject LEFT JOIN expiringObject e USING (obj_id) WHERE obj_id = ? AND (e.valid_until is null or e.valid_until >= now())");
			pstmt.setLong(1, id);
			ResultSet res = pstmt.executeQuery();
			
			if(res.next()){
				res.first();
				rc = new DBGeoObject(
						id,
						res.getString("title"),
						res.getDouble("xPos"), res.getDouble("yPos"),
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
	 * @param limit int the number of retrieved results 
	 * */
	public SearchResult select(String[] tags, BoundingBox box, int limit)
	{
		SearchResult searchResult = new SearchResult();
		ResultSet res;

		try {
			String requestSql = "obj_id, svc_id, o.title, pos[0] AS posx, pos[1] AS posy, t.thumbnail "
				+ "FROM geoObject o INNER JOIN service USING (svc_id) INNER JOIN serviceType t USING (stype_id)";

			//Get the overall result count
			res = queryResult("COUNT(*) FROM geoObject", tags, box, 0, true);
			res.next();
			searchResult.setResultCount(res.getInt(1));
			
			//Get the result set which contains the selected geoObjects.
			res = queryResult(requestSql, tags, box, limit, false);
			
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
					res.getDouble("posx"),
					res.getDouble("posy")));
			}
			
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
	
	private ResultSet queryResult(String requestSql, String[] tags, BoundingBox box, int limit, boolean isCountQuery) throws SQLException {
		String sql;
		
		if (!isCountQuery)
			sql = "SELECT * FROM (";
		else
			sql = "";
		sql += "SELECT " + requestSql + " WHERE ";
		
		//Create the WHERE clause for the geographical location.
		// @> operator: "contains" (pre-postgres 8.2: '@'-operator)
		sql += "pos @ box(point(?, ?), point(?, ?)) AND ";
		
		//Create a where clause for each passed tag.
		if (tags.length > 0)
		{
			for (int i = 0; i < tags.length; i++)
				sql += "obj_id IN (SELECT obj_id FROM objecttag WHERE tag = ?) AND ";
		}
		
		//Remove the last " AND " from the query string again.
		sql = sql.substring(0, sql.length() - 5);
		
		if (limit > 0)
			sql += " ORDER BY RANDOM() LIMIT " + limit; 
		if (!isCountQuery)
			sql += ") AS subquery ORDER BY obj_id ASC";
		
		PreparedStatement stmt = dbConn.prepareStatement(sql);
		
		stmt.setDouble(1, box.getX1());
		stmt.setDouble(2, box.getY1());
		stmt.setDouble(3, box.getX2());
		stmt.setDouble(4, box.getY2());
		for (int i = 0; i < tags.length; i++) {
			stmt.setString(i + 5, tags[i]);
		}		

		return stmt.executeQuery();
	}
	
	/**
	 * Get the Tags for a GeoObject
	 * @param objId Object ID
	 * @return String Array
	 * @throws SQLException
	 */
	private void queryTags(SearchResult result) throws SQLException {
		//Set up the SQL query string.
		String sql = "SELECT obj_id, tag FROM objectTag WHERE obj_id IN (";
		for (int i = 0; i < result.getResults().size(); i++)
			sql += "?, ";
		sql = sql.substring(0, sql.length() - 2);
		sql += ") ORDER BY obj_id ASC";
		
		//Prepare the statement.
		PreparedStatement tagStmt = dbConn.prepareStatement(sql);
		
		//Set up the ID's of the objects.
		for (int i = 0; i < result.getResults().size(); i++)
			tagStmt.setLong(i + 1, result.getResults().get(i).getId());
		
		//Execute the query.
		ResultSet tagRes = tagStmt.executeQuery();
		int objIndex = 0;
		long lastID = -1, curID;
		ArrayList<String> curObjTags = new ArrayList<String>();
		while (tagRes.next())
		{
			curID = tagRes.getLong("obj_id");
			if (curID != lastID)
			{
				if (lastID != -1)
				{
					//We've got a new object.
					//Update the previous object, which is at [objIndex] within the ArrayList.
					result.getResults().get(objIndex).setTags(curObjTags.toArray(new String[curObjTags.size()]));
					curObjTags.clear();
					objIndex++;
				}
				lastID = curID;
			}
			curObjTags.add(tagRes.getString("tag"));
		}
		tagStmt.close();
		
		//Set the tags of the last object in the list.
		result.getResults().get(objIndex).setTags(curObjTags.toArray(new String[curObjTags.size()]));
	}
	
	private String[] queryTags(long objId) throws SQLException {
		PreparedStatement tagStmt = dbConn.prepareStatement("SELECT tag FROM objectTag WHERE obj_id = ?");
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
		PreparedStatement pstmt = dbConn.prepareStatement("SELECT name, value FROM objectProperty WHERE obj_id = ?"); 
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
			PreparedStatement pstmt1 = dbConn.prepareStatement("INSERT INTO geoObject(svc_id, uid, title, link, pos) VALUES (?, ?, ?, ?, ?)");
			pstmt1.setInt(1, obj.getSvc_id());
			pstmt1.setString(2, obj.getUid());
			pstmt1.setString(3, obj.getTitle());
			pstmt1.setString(4, obj.getLink());
			pstmt1.setObject(5, new PGpoint(obj.getXPos(), obj.getYPos()));
			pstmt1.executeUpdate();
			pstmt1.close();
			//save the tags
			PreparedStatement pstmt2 = dbConn.prepareStatement("INSERT INTO objectTag(obj_id, tag) VALUES (currval('geoobject_obj_id_seq'), ?)");
			String tags[] =obj.getTags();
			for(int i=0; i<tags.length; i++){
				pstmt2.setString(1, tags[i].toLowerCase());
				pstmt2.executeUpdate();
			}
			//save the properties
			PreparedStatement pstmt3=dbConn.prepareStatement("INSERT INTO objectProperty (obj_id, name, value) VALUES (currval('geoobject_obj_id_seq'), ?, ?)");
			Property[] prop = obj.getProperties();
			for(int i=0; i<prop.length; i++){
				pstmt3.setString(1, prop[i].getName());
				pstmt3.setString(2, prop[i].getValue());
				pstmt3.executeUpdate();
			}
		}
		catch (SQLException e) {
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
			dbConn = new DBConnection();
			PreparedStatement pstmt = dbConn.prepareStatement("UPDATE geoObject SET svc_id = ?, uid = ?, title = ?, link = ?, pos = point(?,?) WHERE obj_id = ?");
			pstmt.setInt(1, obj.getSvc_id());
			pstmt.setString(2, obj.getUid());
			pstmt.setString(3, obj.getTitle());
			pstmt.setString(4, obj.getLink());
			pstmt.setDouble(5, obj.getXPos());
			pstmt.setDouble(6, obj.getYPos());
			pstmt.setLong(7, obj.getId());
			pstmt.executeUpdate();
			pstmt.close();
		}
		catch (SQLException e) {
			logger.error("SQL error in GeoObjectManager.update()", e);
			throw new RemoteException("SQL error in GeoObjectManager.update()", e);
		}
		catch (IOException e) {
			logger.error("IO exception in GeoObjectManager.update()", e);
			throw new RemoteException("IO exception in GeoObjectManager.update()", e);
		}
	}
}
