package at.fakeroot.sepm.shared.server;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * the ServiceManager provides access to the Database, in order to readout Data from the
 * table service; this class realizes the Singleton pattern
 * @author RK
 */
public class ServiceManager 
{
	private static ServiceManager svcManager = null;
	private static final Logger logger = Logger.getRootLogger();
	
	/**
	 * protected Constructor, tries to connect with DB
	 */
	protected ServiceManager()
	{

	}
	
	/**
	 * returns a reference to the instance of ServiceManager
	 * @return ServiceManager
	 */
	public static ServiceManager getInstance()
	{
		//checks whether the svcManager has been initialized
		if(svcManager == null)
			svcManager = new ServiceManager();
		return svcManager;
	}
	
	/**
	 * returns a DBService object with the Data from the 'service' table of the Database, selected
	 * by the svc_id, which is the parameter for this method
	 * @param svcId int ID of the service 
	 * @return DBService 
	 */
	public DBService select(int svcId) throws SQLException, IOException
	{
		ResultSet rs = null;
		DBService result = null;
		ArrayList<String> tags = new ArrayList<String>();
		DBConnection dbConn = null;
		
		try
		{
			dbConn = new DBConnection();
			//the use of prepared statements in order to avoid sql-injection
			PreparedStatement pstmt = dbConn.prepareStatement("select * from service where svc_id = ?");
			pstmt.setInt(1, svcId);
			rs = pstmt.executeQuery();
			if(rs.next())
			{	
				result = new DBService(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getInt(6), rs.getString(7));
			}
			//the service tags for this object, are readout separately from the table 'serviceTag'
			pstmt = dbConn.prepareStatement("select tag from serviceTag where svc_id = ?");
			pstmt.setInt(1, svcId);
			rs = pstmt.executeQuery();
			while(rs.next())
				tags.add(rs.getString(1));
			result.setTags(tags.toArray(new String[tags.size()]));
		}
		catch (SQLException e)
		{
			logger.error("ServiceManager.select() error", e);
			throw e;
		}
		catch (IOException e) {
			logger.error("ServiceManager.select() IO Exception", e);
			throw e;
		}
		finally {
			if (dbConn != null) dbConn.disconnect();
		}
		
		//returns a DBService with the service tags
		return (result);
		
	}
	
	/**
	 * returns a DBService object with the Data from the 'service' table of the Database, selected
	 * by its name, which is the parameter for this method
	 * @param name String the name of the service
	 * @return DBService
	 */
	public DBService select(String name) throws SQLException, IOException
	{
		ResultSet rs = null;
		DBService result = null;
		DBConnection dbConn = null;
		ArrayList<String> tags = new ArrayList<String>();
		try {
			dbConn = new DBConnection();
			//the use of prepared statements in order to avoid sql-injection
			PreparedStatement pstmt = dbConn.prepareStatement("select * from service where name = ?");
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				result = new DBService(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
					rs.getString(5), rs.getInt(6), rs.getString(7));

				//the service tags for this object, are readout separately from the table 'serviceTag'
				pstmt = dbConn.prepareStatement("select tag from serviceTag where svc_id = ?");
				pstmt.setInt(1, rs.getInt(1));
				rs = pstmt.executeQuery();
				while(rs.next())
					tags.add(rs.getString(1));
				result.setTags(tags.toArray(new String[tags.size()]));
			}
		}
		catch (SQLException e) {
			logger.error("ServiceManager.select() error", e);
			throw e;
		}
		catch (IOException e) {
			logger.error("ServiceManager.select() error", e);
			throw e;
		}
		finally {
			dbConn.disconnect();
		}
		
		//returns a DBService with the service tags
		return (result);
	}
}
