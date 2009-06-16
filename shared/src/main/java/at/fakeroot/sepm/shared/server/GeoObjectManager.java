package at.fakeroot.sepm.shared.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.postgresql.geometric.PGpoint;
import org.apache.log4j.Logger;

import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import at.fakeroot.sepm.shared.client.serialize.ClientGeoObject;
import at.fakeroot.sepm.shared.client.serialize.SearchResult;

/**
 * @author Anca Cismasiu
 * Class representing the DAO for the GeoObjects, that reads from and writes to the database
 * Based on the Singleton pattern.
 */
public class GeoObjectManager 
{
	private static GeoObjectManager geoObjManager = null;
	private DBConnection dbConn;
	PreparedStatement tagStmt;
	private static final Logger logger = Logger.getRootLogger();
	
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
	 * */
	private GeoObjectManager()
	{
		try
		{
			dbConn = new DBConnection();
			tagStmt = dbConn.prepareStatement("SELECT tag FROM objectTag WHERE obj_id = ?");
		}
		catch (Exception e)
		{
			logger.error("Couldn't connect to database", e);
		}
	}

	/**
	 * Method used for obtaining an instance of this Singleton class
	 * */	
	public static GeoObjectManager getInstance()
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
	public long getObjectId(int svc_id, String uid) throws NotFoundException, SQLException {
		
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
	public DBGeoObject getObjectbyId(long id) throws NotFoundException, SQLException {
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
						getTags(id));
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
			String requestSql = "obj_id, svc_id, o.title, pos[0] AS posx, pos[1] AS posy, t.thumbnail";

			// get the overall result count
			res = queryResult("COUNT(*)", tags, box, limit);
			
			res.next();
			searchResult.setResultCount(res.getInt(1));
			
			res = queryResult(requestSql, tags, box, limit);

			while (res.next()) {
				searchResult.addResultToList(new ClientGeoObject(
						res.getInt("obj_id"),
						res.getString("title"),
						res.getString("thumbnail"),
						getTags(res.getInt("obj_id")),
						res.getDouble("posx"),
						res.getDouble("posy")));
			}
		}
		catch (SQLException e) {
			logger.error("SQL error in GeoObjectManager.select()", e);
			searchResult.setErrorMessage(e.getMessage());
		}
	
		return searchResult;
	}
	
	private ResultSet queryResult(String requestSql, String[] tags, BoundingBox box, int limit) throws SQLException {
		String sql = "SELECT "+requestSql+" FROM "
			+ "geoObject o INNER JOIN service USING (svc_id) INNER JOIN serviceType t USING (stype_id) WHERE ";
		int i = 0;
		
		if (tags.length > 0) {
			sql += "obj_id IN (SELECT obj_id FROM objecttag where tag IN (";

			for (i = 0; i < tags.length; i++) {
				sql += "?";
				if (i < tags.length-1) sql += ',';
			}
			
			sql += ") AND ";
		}
		
		// @> operator: "contains" (pre-postgres 8.2: '@'-operator)
		sql += "obj_id IN (select obj_id from geoobject where pos @ box(point(?,?),point(?,?)))";
		
		
		if (tags.length > 0) sql += " GROUP BY obj_id HAVING COUNT(*) = "+tags.length+")";

		if (limit > 0) sql += " LIMIT "+limit; 
		
		
		PreparedStatement stmt = dbConn.prepareStatement(sql);
		
		if (tags.length > 0) {
			for (i = 0; i < tags.length; i++) {
				stmt.setString(i+1, tags[i]);
			}
		}
		stmt.setDouble(++i, box.getX1());
		stmt.setDouble(++i, box.getY1());
		stmt.setDouble(++i, box.getX2());
		stmt.setDouble(++i, box.getY2());


		return stmt.executeQuery();
	}
	
	/**
	 * Get the Tags for a GeoObject
	 * @param objId Object ID
	 * @return String Array
	 * @throws SQLException
	 */
	private String[] getTags(long objId) throws SQLException {
		String[] rc;
		
		tagStmt.setLong(1, objId);
		ResultSet tagRes = tagStmt.executeQuery();
		tagRes.last();
		rc = new String[tagRes.getRow()];
		tagRes.beforeFirst();
		
		while (tagRes.next()) {
			rc[tagRes.getRow()-1] = tagRes.getString(1);
		}

		return rc;
	}

	
	/**
	 * Get the properties for a GeoObject
	 * @param objId Object ID
	 * @return Property Array
	 * @throws SQLException
	 */
	private Property[] getProperties(long obj_id) throws SQLException{
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
		
			return rc;	
			
	} 
	
	
	/**
	 * Insert a new object in the Database
	 * @param obj DBGeoObject object to be inserted
	 * */
	public void insert (DBGeoObject obj)
	{
		try {
			
			PreparedStatement pstmt = dbConn.prepareStatement("INSERT INTO geoObject(svc_id, uid, title, link, pos) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, obj.getSvc_id());
			pstmt.setString(2, obj.getUid());
			pstmt.setString(3, obj.getTitle());
			pstmt.setString(4, obj.getLink());
			pstmt.setObject(5, new PGpoint(obj.getXPos(), obj.getYPos()));
			pstmt.executeUpdate();
			pstmt.close();
			//dbConn.disconnect();
		}
		catch (SQLException e) {
			logger.error("SQL error in GeoObjectManager.insert", e);
		}
	}

	/**
	 * Update an object in the database
	 * @param the new object, that will overwrite the old one with the same object id (obj_id)
	 * */
	public void update (DBGeoObject obj)
	{
		try {
			PreparedStatement pstmt = dbConn.prepareStatement("UPDATE geoObject SET svc_id =?, uid=?, title = ?, link=?, pos=point(?,?) WHERE obj_id = ?");
			pstmt.setInt(1, obj.getSvc_id());
			pstmt.setString(2, obj.getUid());
			pstmt.setString(3, obj.getTitle());
			pstmt.setString(4, obj.getLink());
			pstmt.setDouble(5, obj.getXPos());
			pstmt.setDouble(6, obj.getYPos());
			pstmt.setLong(7, obj.getId());
			pstmt.executeUpdate();
			pstmt.close();
			//dbConn.disconnect();
		}
		catch (SQLException e) {
			logger.error("SQL error in GeoObjectManager.update()", e);
		}
	}
}
