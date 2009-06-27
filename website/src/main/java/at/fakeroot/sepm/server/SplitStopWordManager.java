package at.fakeroot.sepm.server;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class SplitStopWordManager {

	private static SplitStopWordManager sswManager = null;
	private static final Logger logger = Logger.getRootLogger();
	
	
	public static SplitStopWordManager getInstance(){
		//checks whether the swManager has been initialized
		if(sswManager==null){
			sswManager= new SplitStopWordManager();
		}
		return sswManager;
	}
	
	/**
	 *
	 * @return Returns the StopWords (German)
	 */
	public String[] getStopWords()  throws SQLException, IOException{
		/*
		ResultSet rs = null;
		String[] result = null;
		ArrayList<String> stopWords = new ArrayList<String>();
		DBConnection dbConn = null;
		
		try
		{
			dbConn = new DBConnection();
			
			PreparedStatement pstmt = dbConn.prepareStatement("select * from stopWords ORDER BY word ASC");
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				stopWords.add(rs.getString(1));
			}
			result = stopWords.toArray(new String[stopWords.size()]);
		}
		catch (SQLException e)
		{
			logger.error("StopWordManager.getStopWords() error", e);
			throw e;
		}
		catch (IOException e) {
			logger.error("StopWordManager.getStopWords() IO Exception", e);
			throw e;
		}
		finally {
			if (dbConn != null) dbConn.disconnect();
		}
		
		return result;
		*/
		
		return new String[] {"test"};
		
	}
	
	/**
	 * 
	 * @return Returns SplitCharacters
	 */
	public String getSplitChars()  throws SQLException, IOException{
		/*
		ResultSet rs = null;
		String result = null;
		DBConnection dbConn = null;
		
		try
		{
			dbConn = new DBConnection();
			
			PreparedStatement pstmt = dbConn.prepareStatement("select * from splitChars");
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				result+=rs.getString(1);
			}
		}
		catch (SQLException e)
		{
			logger.error("StopWordManager.getSplitChars() error", e);
			throw e;
		}
		catch (IOException e) {
			logger.error("StopWordManager.getSplitChars() IO Exception", e);
			throw e;
		}
		finally {
			if (dbConn != null) dbConn.disconnect();
		}
		
		return result;
		*/
		
		
		return " <>|^°!\"§$%&/{([)]=}?\\´`+*~#'-_.:,;" +
		new Character((char)10).toString() + new Character((char)13).toString();
		
	}
}
