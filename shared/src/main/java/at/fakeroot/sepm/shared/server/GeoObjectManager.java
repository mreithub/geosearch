package at.fakeroot.sepm.shared.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	 * Method used to check if the DBGeoObject already exists in the database
	 * @param obj DBGeoObject object we are searching for
	 * @return DBGeoObject the existing object, or null if there is no previous record in the database with these values
	 * */

	public DBGeoObject select(DBGeoObject obj)
	{
		ResultSet rs1=null;
		ResultSet rs2=null;
		ResultSet rs3=null;
		DBGeoObject rc=null;

		try {	
			String query = "SELECT obj_id, svc_id, uid, title, link, pos, last_updated FROM geoObject WHERE 1=1";
			if(obj.getId()!=0)
				query+=" AND obj_id= ?";
			if(obj.getSvc_id()!=0)
				query+=" AND svc_id = ?";
			if(obj.getUid()!=null&& !obj.getUid().equals(""))
				query+=" AND uid = ?";
			
			PreparedStatement pstmt1 = dbConn.prepareStatement(query+";");
			pstmt1.setLong(1, obj.getId());
			pstmt1.setInt(2, obj.getSvc_id());
			pstmt1.setString(3, obj.getUid());
			rs1=pstmt1.executeQuery();
			pstmt1.close();
			dbConn.disconnect();
	
			PreparedStatement pstmt2 = dbConn.prepareStatement("SELECT name, value FROM objectProperty WHERE obj_id = ?");
			pstmt2.setLong(1, obj.getId());
			rs2=pstmt2.executeQuery();
			pstmt2.close();
			dbConn.disconnect();
			rs2.last();
			int propRowCount = rs2.getRow();
			rs2.first();
			
	
			PreparedStatement pstmt3 = dbConn.prepareStatement("SELECT tag FROM objectTag WHERE obj_id=?");
			pstmt3.setLong(1, obj.getId());
			rs3=pstmt3.executeQuery();
			rs3=pstmt3.executeQuery();
			rs3.last();
			int tagRowCount = rs3.getRow();
			rs3.first();
			pstmt3.close();
			dbConn.disconnect();
	
			if(rs1.next()){
				PGpoint location = (PGpoint)rs1.getObject(6);
	
				Property[] properties= new Property[propRowCount];
				int i=0;
				while(rs2.next()){
					properties[i]=new Property(rs2.getString(2), rs2.getString(3));
					i++;
				}
	
				String[] tags = new String[tagRowCount];
				int j=0;
				while(rs3.next()){
					tags[j]=rs3.getString(2);
					j++;
				}
	
				rc= new DBGeoObject(rs1.getInt(1), rs1.getString(4), location.x ,location.y, rs1.getInt(2), rs1.getString(3), rs1.getString(5),rs1.getTimestamp(7), properties, tags);
			}
		} catch (SQLException e) {
			logger.error("SQLException in GeoObjectManager.select()", e);
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

		try {
			String sql = "SELECT obj_id, svc_id, o.title, pos[0] AS posx, pos[1] AS posy, t.thumbnail FROM "
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


			ResultSet res = stmt.executeQuery();

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
	
	/**
	 * Get the Tags for a GeoObject
	 * @param objId Object ID
	 * @return String Array
	 * @throws SQLException
	 */
	private String[] getTags(int objId) throws SQLException {
		String[] rc;
		
		tagStmt.setInt(1, objId);
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
	 * Insert a new object in the Database
	 * @param obj DBGeoObject object to be inserted
	 * */
	public void insert (DBGeoObject obj)
	{
		try {
			PreparedStatement pstmt = dbConn.prepareStatement("INSERT INTO geoObject(svc_id, uid, title, link, pos) VALUES (?, '?', '?', '?', ?)");
			pstmt.setInt(1, obj.getSvc_id());
			pstmt.setString(2, obj.getUid());
			pstmt.setString(3, obj.getTitle());
			pstmt.setString(4, obj.getLink());
			pstmt.setObject(5, new PGpoint(obj.getXPos(), obj.getYPos()));
			pstmt.executeUpdate();
			pstmt.close();
			dbConn.disconnect();
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
			PreparedStatement pstmt = dbConn.prepareStatement("UPDATE geoObject SET svc_id =?, uid='?', title = '?', link='?', pos='(?,?)' WHERE ID = ?");
			pstmt.setInt(1, obj.getSvc_id());
			pstmt.setString(2, obj.getUid());
			pstmt.setString(3, obj.getTitle());
			pstmt.setString(4, obj.getLink());
			pstmt.setDouble(5, obj.getXPos());
			pstmt.setDouble(6, obj.getYPos());
			pstmt.setLong(7, obj.getId());
			pstmt.executeUpdate();
			pstmt.close();
			dbConn.disconnect();
		}
		catch (SQLException e) {
			logger.error("SQL error in GeoObjectManager.update()", e);
		}
	}
}
