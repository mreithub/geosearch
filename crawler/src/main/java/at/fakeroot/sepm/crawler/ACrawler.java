package at.fakeroot.sepm.crawler;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import at.fakeroot.sepm.shared.server.DBGeoObject;

/**
 * Abstract Crawler Class. Includes all necessary functions for crawling, and the communication with the server.   
 * @author JB
 *
 */
public abstract class ACrawler  {
	private static final Logger logger = Logger.getRootLogger();
	private BoundingBox curBox;
	private boolean newCircle=false;
	// words that shouldn't be used as tags
	private static String[] stopWords;
	// characters to split words
	private static String splitChars;
	private CrawlerOutput crawlerOutput;
	

	//
	// default property values
	//  (will be overwritten with the contents of crawler.properties
	//  and <crawler-name>.properties)
	//
	/**
	 * Area to crawl in
	 */
	private BoundingBox crawlArea = new BoundingBox(0, -90, 360, 90);
	
	/**
	 * X size of the crawl rectangles
	 */
	private double xOffset=0.5;
	
	/**
	 * Y size of the crawl rectangles
	 */
	private double yOffset=0.5;
	
	/**
	 *  time between two crawlBox() calls (msec)
	 */
	private long interval=1000;

	/**
	 * API key for the service (if any)
	 */
	private String apiKey = null;

	/**
	 * security key of the service (if any)
	 */
	private String secKey = null;
	
	
	/**
	 * output method
	 */
	private String outputMethod = "SQL";
	
	private Properties myProperties = new Properties();
	
	/**
	 * Standard constructor 
	 * * Step size is 0.5
	 * * ServerHost is localhost
	 * * ServerPort is RMI-Std. Port
	 * @param SvcName Service Name (eg. Wiki_de, Panoramio)
	 * @throws IOException 
	 * @throws NotBoundException if RMI output is used and the RMI connection couldn't be established
	 */
	public ACrawler(String svcName) throws IOException, NotBoundException {
		init(svcName);
	}
	
	/**
	 * private init function called by all Constructors 
	 * @param svcName service name
	 * @throws IOException e.g. when crawler.properties couldn't be read
	 * @throws NotBoundException if RMI output is used and the RMI connection couldn't be established
	 */
	private void init(String svcName) throws IOException, NotBoundException {
		logger.info("Crawler "+svcName+" started");
		
		// more specific properties overwrite the global ones
		loadProperties("crawler.properties");
		try {
			loadProperties(svcName+".properties");
		}
		catch (IOException e) {
			logger.info("Couldn't open "+svcName+".properties", e);
		}
		
		readProperties();
	
		if(outputMethod.equals("SQL")){
			crawlerOutput=new SQLOutput(svcName, myProperties);
		}else if(outputMethod.equals("RMI")){
			crawlerOutput=new RMIOutput(svcName, myProperties);
		}
		

		stopWords=crawlerOutput.getStopWords();
		splitChars=crawlerOutput.getSplitChars();
	}
	
	/**
	 * reads a properties file
	 */
	private void loadProperties(String filename) throws IOException {
		// 
		Properties prop = new Properties(myProperties);
		InputStream propStream = getClass().getResourceAsStream("/"+filename);
		
		if (propStream == null) {
			logger.error("Error: Couldn't open property file '"+filename+"'");
			throw new IOException("Error: Couldn't open property file '"+filename+"'");
		}
		prop.load(propStream);
		myProperties = prop;
	}
	
	/**
	 * Interpretes the previously by loadProperties() loaded properties
	 * 
	 * @throws IOException
	 */
	private void readProperties() throws IOException {
		crawlArea=new BoundingBox(
				Double.parseDouble(myProperties.getProperty("crawler.box.x1", Double.toString(crawlArea.getX1()))),
				Double.parseDouble(myProperties.getProperty("crawler.box.y1", Double.toString(crawlArea.getY1()))),
				Double.parseDouble(myProperties.getProperty("crawler.box.x2", Double.toString(crawlArea.getX2()))),
				Double.parseDouble(myProperties.getProperty("crawler.box.y2", Double.toString(crawlArea.getY2()))));
		
		xOffset = Double.parseDouble(myProperties.getProperty("crawler.stepSize", Double.toString(xOffset)));
		yOffset = xOffset;

		interval = Long.parseLong(myProperties.getProperty("crawler.interval", Long.toString(interval)));

		curBox = crawlArea;
		apiKey = myProperties.getProperty("crawler.apiKey", apiKey);
		secKey = myProperties.getProperty("crawler.secKey", secKey);
		

		outputMethod = myProperties.getProperty("crawler.output",outputMethod);
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
				logger.error("Error: Aborted unexpectedly",e);
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
		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();
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
			logger.error("Fatal protocol violation",e);
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Fatal transport error",e);
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
	private BoundingBox nextCrawlBox(){		
		return nextBox(curBox);
	}
	
	
	/**
	 * Send an array of DBGeoObjects to the Server
	 * @param newObjects Array of new Objects
	 */
	protected void saveObject(DBGeoObject[] newObjects) {		
		
		crawlerOutput.saveObjects(newObjects);
		
	}
	
	public String toString() {
		String showStopWord = "";
		for(int i=0;i<stopWords.length;i++){
			showStopWord+=", "+stopWords[i];
		}
		return "curBox: "+curBox.toString()+", outputMethod: "+outputMethod+", stopWords: "+showStopWord+", splitChars: "+splitChars;
	}
	

	
	/**
	 * internal function to calculate the next BoundingBox to crawl
	 * @param _curPos current BoundingBox
	 * @return next BoundingBox
	 */
	private BoundingBox nextBox(BoundingBox _curPos){
		curBox=nextRight(_curPos);
		
		if(newCircle){
			curBox=nextDown(curBox);
			newCircle=false;
		}
		
		return curBox;
		
	}
	
	/**
	 * Get the BoundingBox right of the current one 
	 * @param _curPos current BoundingBox
	 * @return new BoundingBox right to _curPos
	 */
	private BoundingBox nextRight(BoundingBox _curPos){
		if(_curPos.getX1()+xOffset>crawlArea.getX2()){
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
	
	/**
	 * get the BoundingBox below the current one
	 * @param _curPos current BoundingBox
	 * @return new BoundingBox below the current one
	 */
	private BoundingBox nextDown(BoundingBox _curPos){
		if(_curPos.getY1()+yOffset>crawlArea.getY2()){
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
	 * Here you have to create the own crawler.
	 * @param curBox
	 */
	protected abstract void crawlBox(BoundingBox curBox);
}
