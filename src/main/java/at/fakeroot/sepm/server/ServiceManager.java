package at.fakeroot.sepm.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ServiceManager 
{
	private static ServiceManager svcManager = null;
	private DBConnection dbConn;
	
	protected ServiceManager()
	{
		try
		{
			dbConn = new DBConnection();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static ServiceManager getInstance()
	{
		if(svcManager == null)
			svcManager = new ServiceManager();
		return svcManager;
	}
	
	public DBService select(int svcId)
	{
		ResultSet rs = null;
		DBService result = null;
		ArrayList<String> tags = new ArrayList<String>();
		
		try
		{
			PreparedStatement pstmt = dbConn.prepareStatement("select * from service where svc_id = ?");
			pstmt.setInt(1, svcId);
			rs = pstmt.executeQuery();
			if(rs.next())
			{	
				result = new DBService(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getInt(6), rs.getString(7));
			}
			pstmt = dbConn.prepareStatement("select tag from serviceTag where svc_id = ?");
			pstmt.setInt(1, svcId);
			rs = pstmt.executeQuery();
			while(rs.next())
				tags.add(rs.getString(1));
			result.setTags(tags.toArray(new String[tags.size()]));
			dbConn.disconnect();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return (result);
		
	}
	
	public DBService select(String name)
	{
		ResultSet rs = null;
		DBService result = null;
		ArrayList<String> tags = new ArrayList<String>();
		try
		{
			PreparedStatement pstmt = dbConn.prepareStatement("select * from service where name = ?");
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				result = new DBService(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
					rs.getString(5), rs.getInt(6), rs.getString(7));
			}
			pstmt = dbConn.prepareStatement("select tag from serviceTag where svc_id = ?");
			pstmt.setInt(1, rs.getInt(1));
			rs = pstmt.executeQuery();
			while(rs.next())
				tags.add(rs.getString(1));
			result.setTags(tags.toArray(new String[tags.size()]));
			dbConn.disconnect();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return (result);
	}
}
