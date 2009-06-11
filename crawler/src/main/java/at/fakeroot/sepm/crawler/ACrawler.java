package at.fakeroot.sepm.crawler;
import java.io.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
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

import com.google.gwt.maps.client.geom.LatLng;

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
	
	// time between two crawlBox() calls (msec)
	private long interval=1000;

	private boolean newCircle=false;
	
	private int serviceID;
	private String apiKey = null;
	private String secKey = null;
	
	private String rmiServer = "localhost";
	private int rmiPort = 1099;

	// Create an instance of HttpClient.
	private HttpClient client = new HttpClient();
	
	private static String[] stopWords = new String[] {
		"aber", "als", "am", "an", "auch", "auf", "aus", "bei", "bin", "bis", "bist", "da", "dadurch",
		"daher", "darum", "das", "daß", "dass", "dein", "deine", "dem", "den", "der", "des", "dessen",
		"deshalb", "die", "dies", "dieser", "dieses", "doch", "dort", "du", "durch", "ein", "eine", "einem",
		"einen", "einer", "eines", "er", "es", "euer", "eure", "für", "hatte", "hatten", "hattest", "hattet",
		"hier", "hinter", "ich", "ihr", "ihre", "im", "in", "ist", "ja", "jede", "jedem", "jeden", "jeder",
		"jedes", "jener", "jenes", "jetzt", "kann", "kannst", "können", "könnt", "machen", "mein", "meine",
		"mit", "muß", "mußt", "musst", "müssen", "müßt", "nach", "nachdem", "nein", "nicht", "nun", "oder",
		"seid", "sein", "seine", "sich", "sie", "sind", "soll", "sollen", "sollst", "sollt", "sonst", "soweit",
		"sowie", "über", "und", "unser", "unsere", "unter", "vom", "von", "vor", "wann", "warum", "was", "weiter",
		"weitere", "wenn", "wer", "werde", "werden", "werdet", "weshalb", "wie", "wieder", "wieso", "wir",
		"wird", "wirst", "wo", "woher", "wohin", "zu", "zum", "zur" };
	
	//Define the split characters. Include characters [10] and [13] in order to split at line breaks.
	private static String splitChars = " <>|^°!\"§$%&/{([)]=}?\\´`+*~#'-_.:,;" +
		new Character((char)10).toString() + new Character((char)13).toString();

	
	private IGeoSave geoSaver;
	
	/**
	 * Standard constructor 
	 * * Step size is 0.5
	 * * ServerHost is localhost
	 * * ServerPort is RMI-Std. Port
	 * @param SvcName Service Name (eg. Wiki_de, Panoramio)
	 * @throws IOException 
	 */
	public ACrawler(String svcName) throws IOException {
		init(svcName);
	}
	
	private void init(String svcName) throws IOException {
		// specific properties overwrite the global ones
		loadProperties("crawler.properties");
		try {
			loadProperties(svcName+".properties");
		}
		catch (IOException e) {
			// no service-specific config file
		}

		// init RMI		
		Registry reg = LocateRegistry.getRegistry(rmiServer, rmiPort);
		for(int i=0;i<reg.list().length;i++){
			System.out.println("reg: "+reg.list()[i]);
		}
		try {
			//geoSaver = (IGeoSave) reg.lookup("rmi://"+rmiServer+":"+rmiPort+"/IGeoSave");
			geoSaver = (IGeoSave) reg.lookup("IGeoSave");
		}
		catch (Exception e) {
			// if we can't get a RMI connection, enter testing mode
			geoSaver = new GeoSaveTest();
		}
		

		// Request Service ID
		//System.out.println("id: "+requestServiceID(svcName));
		serviceID=requestServiceID(svcName);
	}
	
	/**
	 * Reads the Object's properties from a file
	 * @param filename file to read
	 * @throws IOException
	 */
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

		interval = Long.parseLong(prop.getProperty("crawler.interval", Long.toString(interval)));

		curBox = crawlArea;
		apiKey = prop.getProperty("crawler.apiKey", apiKey);
		secKey = prop.getProperty("crawler.secKey", secKey);
		
		rmiServer = prop.getProperty("rmi.server", rmiServer);
		rmiPort = Integer.parseInt(prop.getProperty("rmi.port", Integer.toString(rmiPort)));

	}
	
	/**
	 * infinite loop that calls the crawler-specific crawl function
	 */
	public void crawl() {
		while(true){
			crawlBox(nextCrawlBox());
			try {
				Thread.sleep(interval);
			}
			catch (InterruptedException e) {
				System.err.println("Error: Aborted unexpectedly");
				System.exit(1);
			}
		}
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
				//Check if we can accept this word. We do that if it's not a stop-word, and
				//if it starts with an upper-case letter (because these words are usually substantives
				//in the German language).
				if (splits[i].substring(0, 1).equals(splits[i].substring(0, 1).toLowerCase()))
					continue;
				
				String lowcaseSplit = splits[i].toLowerCase();
				boolean isStopWord = false;
				for (int j = 0; j < stopWords.length; j++)
				{
					if (stopWords[j].equals(lowcaseSplit))
					{
						isStopWord = true;
						break;
					}
				}
				if (isStopWord)
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
				
		for (int i = 0; i < source.length(); i++)
		{
			Character curChar = source.charAt(i);
			//If the current character is a split character, add the current string to the result list.
			if (splitChars.contains(curChar.toString()))
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
	 * Computes the approximate distance in kilometers between two given geographical points. 
	 * @param p1
	 * @param p2
	 * @return
	 */
	protected double computeDistance(LatLng p1, LatLng p2)
	{
		double x1, y1, z1, x2, y2, z2;
		double rEarth = 40000;
		
		double longArg = (p1.getLongitude() - 90) * Math.PI / 180;
		double latArg = p1.getLatitude() * Math.PI / 180;
		x1 = Math.cos(longArg) * Math.cos(latArg) * rEarth;
		y1 = Math.sin(longArg) * Math.cos(latArg) * rEarth;
		z1 = Math.sin(latArg) * rEarth;
		
		longArg = (p2.getLongitude() - 90) * Math.PI / 180;
		latArg = p2.getLatitude() * Math.PI / 180;
		x2 = Math.cos(longArg) * Math.cos(latArg) * rEarth;
		y2 = Math.sin(longArg) * Math.cos(latArg) * rEarth;
		z2 = Math.sin(latArg) * rEarth;
		
		return (Math.acos((x1 * x2 + y1 * y2 + z1 * z2) / (Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1) * Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2))) * rEarth);
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
