package at.fakeroot.sepm.crawler;
import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Properties;

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
	private BoundingBox crawlArea = new BoundingBox(0, -90, 360, 90);
	private BoundingBox curBox;
	
	private double xOffset=0.5;
	private double yOffset=0.5;
	
	private boolean newCircle=false;
	
	private int serviceID;
	private String apiKey = null;
	private String secKey = null;
	
	private String rmiServer = "localhost";
	private int rmiPort = 1099;

	// Create an instance of HttpClient.
	private HttpClient client = new HttpClient();
	
	private IGeoSave geoSaver;
	
	/**
	 * Standard constructor 
	 * * Step size is 0.5
	 * * ServerHost is localhost
	 * * ServerPort is RMI-Std. Port
	 * @param SvcName Service Name (eg. Wiki_de, Panoramio)
	 * @throws IOException 
	 */
	public ACrawler(String svcName) throws IOException, NotBoundException  {
		init(svcName);
	}
	
	private void init(String svcName) throws IOException, NotBoundException {
		// specific properties overwrite the global ones
		loadProperties("crawler.properties");
		loadProperties(svcName+".properties");

		// init RMI
		Registry reg = LocateRegistry.getRegistry(rmiServer, rmiPort);
		geoSaver = (IGeoSave) reg.lookup("GeoSave");

		// Request Service ID
		serviceID=requestServiceID(svcName);
		
		while(true){
			crawlBox(nextCrawlBox());
		}
	}
	
	private void loadProperties(String filename) throws IOException {
		Properties prop = new Properties();
		InputStream propStream = getClass().getResourceAsStream("/"+filename);
		
		if (propStream == null) {
			throw new IOException("Error: Couldn't open property file '"+filename+"'");
		}
		prop.load(propStream);

		crawlArea=new BoundingBox(
				Double.parseDouble(prop.getProperty("crawler.box.x1", Double.toString(crawlArea.getX1()))),
				Double.parseDouble(prop.getProperty("crawler.box.y1", Double.toString(crawlArea.getY1()))),
				Double.parseDouble(prop.getProperty("crawler.box.x2", Double.toString(crawlArea.getX2()))),
				Double.parseDouble(prop.getProperty("crawler.box.y2", Double.toString(crawlArea.getY2()))));
		
		xOffset = Double.parseDouble(prop.getProperty("crawler.stepSize", Double.toString(xOffset)));
		yOffset = xOffset;

		curBox = crawlArea;
		apiKey = prop.getProperty("crawler.apiKey", apiKey);
		secKey = prop.getProperty("crawler.secKey", secKey);
		
		rmiServer = prop.getProperty("rmi.server", rmiServer);
		rmiPort = Integer.parseInt(prop.getProperty("rmi.port", Integer.toString(rmiPort)));

	}
	
	/**
	 * Requests a URL via HTTP
	 * @param url URL that should be fetched.  
	 * @return Response String (eg. xml, json, ...)
	 */
	protected String requestUrl(String url){
		// Create a method instance.
		GetMethod method = new GetMethod(url);
		
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
	
	private int requestServiceID(String svcName) {
		int rc;
		try {
			rc = geoSaver.getServiceID(svcName);
		} catch (RemoteException e) {
			e.printStackTrace();
			return -1;
		}

		return rc;
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
		if(_curPos.getX1()+xOffset>crawlArea.getX2()){
			//System.err.println("rumpRight "+_curPos);
			newCircle=true;
			return new BoundingBox(
					crawlArea.getX1(),
					_curPos.getY1(),
					crawlArea.getX1()+xOffset,
					_curPos.getY2());
		}else{
			return new BoundingBox(
				_curPos.getX2(),
				_curPos.getY1(),
				_curPos.getX2()+xOffset,
				_curPos.getY2());
		}
		
		
	}
	
	private BoundingBox nextDown(BoundingBox _curPos){
		if(_curPos.getY1()+yOffset>crawlArea.getY2()){
			//System.err.println("JumpDown ");
			return new BoundingBox(
					_curPos.getX1(),
					crawlArea.getY1(),
					_curPos.getX2(),
					crawlArea.getY1()+yOffset);
		}else{		
			return new BoundingBox(
				_curPos.getX1(),
				_curPos.getY2(),
				_curPos.getX2(),
				_curPos.getY2()+yOffset);
		}
	}
	
	private BoundingBox nextLeft(BoundingBox _curPos){
		return new BoundingBox(
				_curPos.getX1()-xOffset,
				_curPos.getY1(),
				_curPos.getX1(),
				_curPos.getY2());
	}
	
	private BoundingBox nextUp(BoundingBox _curPos){
		return new BoundingBox(
				_curPos.getX1(),
				_curPos.getY1()-yOffset,
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

	/**
	 * Requests the API key for the service (which is stored in a property file) 
	 * @return API key for the service name passed to the constructor
	 */
	protected String getApiKey() {
		return apiKey;
	}
	
	/**
	 * Returns a SecurtiyKey. Else NULL
	 * @return SecurityKey
	 */
	protected String getSecKey(){
		return secKey;
	}
	
	/**
	 * Hier muss der Eigene Crawler gemacht werden.
	 * @param curBox
	 */
	protected abstract void crawlBox(BoundingBox curBox);
}
