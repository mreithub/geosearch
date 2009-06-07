package at.fakeroot.sepm.crawler;
import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import at.fakeroot.sepm.shared.server.DBGeoObject;
import at.fakeroot.sepm.shared.server.IGeoSave;

/**
 * Abstract Crawler Class. Includes all necessary functions for crawling, and the communication with the server.   
 * @author JB
 *
 */
public abstract class ACrawler  {
	private BoundingBox crawlArea;
	private BoundingBox curBox;
	
	private double XOFFSET=0.5;
	private double YOFFSET=0.5;
	
	private boolean newCircle=false;
	
	private int serviceID;
	
	// Create an instance of HttpClient.
	private HttpClient client = new HttpClient();
	
	private Registry registry;
	private IGeoSave geoSaver;
	
	
	//BoundingBoxes
	protected static BoundingBox AUSTRIA = new BoundingBox(9.25, 46.39, 17.34, 49.05);
	
	/**
	 * Standard constructor 
	 * * BoundingBox is AUSTRIA,
	 * * Step size is 0.5
	 * * ServerHost is localhost
	 * * ServerPort is RMI-Std. Port
	 * @param SvcName Service Name (eg. Wiki_de, Panoramio)
	 */
	public ACrawler(String SvcName) {
		this(AUSTRIA, 0.5, SvcName);
	}	
	
	/**
	 * Standard constructor 
	 * * Step size is 0.5
	 * * ServerHost is localhost
	 * * ServerPort is RMI-Std. Port
	 * @param _crawlArea Search Area (eg. Austria, Europe, Word)
	 * @param SvcName Service Name (eg. Wiki_de, Panoramio)
	 */
	public ACrawler(BoundingBox _crawlArea, String SvcName){
		this(_crawlArea, 0.5, SvcName);
	}
	
	/**
	 * Standard constructor 
	 * * ServerHost is localhost
	 * * ServerPort is RMI-Std. Port
	 * @param _crawlArea Search Area (eg. Austria, Europe, Word)
	 * @param _jumpXYOffset Step size 
	 * @param SvcName Service Name (eg. Wiki_de, Panoramio)
	 */
	public ACrawler(BoundingBox _crawlArea, double _jumpXYOffset, String SvcName) {		
		this(_crawlArea, _jumpXYOffset, "localhost", SvcName);
		
	}
	
	/**
	 * Standard constructor 
	 * * ServerPort is RMI-Std. Port
	 * @param _crawlArea Search Area (eg. Austria, Europe, Word)
	 * @param _jumpXYOffset Step size 
	 * @param ServerHost ServerHost name (eg. localhost, www.myCrawler.com, 127.0.0.1)
	 * @param SvcName Service Name (eg. Wiki_de, Panoramio)
	 */
	public ACrawler(BoundingBox _crawlArea, double _jumpXYOffset, String ServerHost, String SvcName) {
		 this(_crawlArea, _jumpXYOffset, ServerHost, 1099, SvcName);
	}
	
	/**
	 * Standard constructor 
	 * @param _crawlArea Search Area (eg. Austria, Europe, Word)
	 * @param _jumpXYOffset Step size 
	 * @param ServerHost ServerHost name (eg. localhost, www.myCrawler.com, 127.0.0.1)
	 * @param ServerPort Server Port.  (Std. Port is 1099)
	 * @param SvcName Service Name (eg. Wiki_de, Panoramio)
	 */
	public ACrawler(BoundingBox _crawlArea, double _jumpXYOffset, String ServerHost, int ServerPort, String SvcName) {
		crawlArea=_crawlArea;
		XOFFSET=_jumpXYOffset;
		YOFFSET=_jumpXYOffset;
		curBox = new BoundingBox(
				_crawlArea.getX1(),
				_crawlArea.getY1(),
				_crawlArea.getX1()+XOFFSET,
				_crawlArea.getY1()+YOFFSET);
		
		/*try {
			registry = LocateRegistry.getRegistry(ServerHost);
			geoSaver = (GeoSave) registry.lookup("GeoSave");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} 
		
		serviceID=getServiceID(SvcName);*/
	}
	
	/**
	 * Crawls a site at the given URL.
	 * @param crawlURL URL that should be crawled.  
	 * @return Response String (eg. xml, json,)
	 */
	protected String crawl(String crawlURL){
		// Create a method instance.
		GetMethod method = new GetMethod(crawlURL);
		
		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));

		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}

			
			// Read the response body.
			byte[] responseBody = method.getResponseBody();

			
			
			return new String(responseBody);
			
			/*
			InputStream myStream = method.getResponseBodyAsStream();			
			BufferedReader in = new BufferedReader(new InputStreamReader(myStream));
			//in.readLine();
			String back="";
			while(in.ready()){
				back+=in.readLine();
			}
			System.out.println(back);
			*/
			
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Release the connection.
			method.releaseConnection();
		}	
		return null;
	}
	
	
	/**
	 * 
	 * @return Next BoundingBox in the gives search area. 
	 */
	protected BoundingBox nextCrawlBox(){		
		return nextBox(curBox);
	}
	
	
	/**
	 * Send an array of DBGeoObjects to the Server
	 * @param newObjects Array of new Objects
	 */
	protected void saveObject(DBGeoObject[] newObjects) {		
		
		try {
			geoSaver.saveObjects(newObjects);
		} catch (RemoteException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Returns the ServiceID
	 * @return Returns the ServiceID
	 */
	protected int getSvcID(){
		return serviceID;
	}
	
	public String toString() {
		return curBox.toString();
	}
	
	private int getServiceID(String SvcName){
		
		try {
			return geoSaver.getServiceID(SvcName);
		} catch (RemoteException e) {
			e.printStackTrace();
			return -1;
		} 
	}
	
	private BoundingBox nextBox(BoundingBox _curPos){
		
		curBox=nextRight(_curPos);
		
		if(newCircle){
			//System.out.println("rein");
			curBox=nextDown(curBox);
			newCircle=false;
		}
		
		return curBox;
		
	}
	
	private BoundingBox nextRight(BoundingBox _curPos){
		if(_curPos.getX1()+XOFFSET>crawlArea.getX2()){
			//System.err.println("rumpRight "+_curPos);
			newCircle=true;
			return new BoundingBox(
					crawlArea.getX1(),
					_curPos.getY1(),
					crawlArea.getX1()+XOFFSET,
					_curPos.getY2());
		}else{
			return new BoundingBox(
				_curPos.getX2(),
				_curPos.getY1(),
				_curPos.getX2()+XOFFSET,
				_curPos.getY2());
		}
		
		
	}
	
	private BoundingBox nextDown(BoundingBox _curPos){
		if(_curPos.getY1()+YOFFSET>crawlArea.getY2()){
			//System.err.println("JumpDown ");
			return new BoundingBox(
					_curPos.getX1(),
					crawlArea.getY1(),
					_curPos.getX2(),
					crawlArea.getY1()+YOFFSET);
		}else{		
			return new BoundingBox(
				_curPos.getX1(),
				_curPos.getY2(),
				_curPos.getX2(),
				_curPos.getY2()+YOFFSET);
		}
	}
	
	private BoundingBox nextLeft(BoundingBox _curPos){
		return new BoundingBox(
				_curPos.getX1()-XOFFSET,
				_curPos.getY1(),
				_curPos.getX1(),
				_curPos.getY2());
	}
	
	private BoundingBox nextUp(BoundingBox _curPos){
		return new BoundingBox(
				_curPos.getX1(),
				_curPos.getY1()-YOFFSET,
				_curPos.getX2(),
				_curPos.getY1());
	}
	
	
	/**
	 * This function parses a given string into tags.
	 * @param source The source string.
	 * @param tags The ArrayList of tags where newly found tags are added to.
	 * @param checkValidity If true, only words which pass a validity check are accepted as tags. Note that
	 * this validity check only works well for source strings which are in German. Pass false for this parameter
	 * if you want to include all words of this string as tags.
	 */
	protected void parseStringIntoTags(String source, ArrayList<String> tags, boolean checkValidity)
	{
		//Split the string into words.
		String[] splits = splitString(source);
		for (int i = 0; i < splits.length; i++)
		{
			if (checkValidity)
			{
				//Check if we can accept this word. We do that if it's longer than 3 chars, and
				//if it starts with an upper-case letter (because these words are usually substantives
				//in the German language).
				if (splits[i].length() <= 3 || splits[i].substring(0, 1).equals(splits[i].substring(0, 1).toLowerCase()))
					continue;
			}
			//Check if this tag has already been found so far.
			if (!tags.contains(splits[i]))
				tags.add(splits[i]);
		}
	}
	
	
	/**
	 * Split a given string into separate words. This function considers additional splitting requirements
	 * which are required for the tag extracting process.
	 * @param source The source string.
	 * @return A String array which holds the splitted words.
	 */
	private String[] splitString(String source)
	{
		String curString = "";
		ArrayList<String> resultList = new ArrayList<String>();
		
		//Define the split characters. Include characters [10] and [13] in order to split at line breaks.
		String invalidChars = " <>|^°!\"§$%&/{([)]=}?\\´`+*~#'-_.:,;" +
			new Character((char)10).toString() + new Character((char)13).toString();
		
		for (int i = 0; i < source.length(); i++)
		{
			Character curChar = source.charAt(i);
			//If the current character is a split character, add the current string to the result list.
			if (invalidChars.contains(curChar.toString()))
			{
				if (curString.length() > 0)
					resultList.add(curString);
				curString = "";
			}
			else
				curString += curChar;
		}
		if (curString.length() > 0)
			resultList.add(curString);
		
		//Return the result as array.
		return (resultList.toArray(new String[resultList.size()]));
	}	
}
