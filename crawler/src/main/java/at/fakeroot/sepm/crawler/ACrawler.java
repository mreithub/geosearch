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
	 * @throws IOException if the Properties-file couldn't be read
	 * @throws NotBoundException if RMI output is used and the RMI connection couldn't be established
	 */
	public ACrawler(String svcName) throws IOException, NotBoundException {
		init(svcName, null);
	}
	
	/**
	 * constructor for testing purposes
	 * @param svcName service name
	 * @param output output method
	 * @throws IOException if the Properties-file couldn't be read
	 * @throws NotBoundException if RMO output is used and the RMI connection couldn't be established
	 */
	public ACrawler(String svcName, CrawlerOutput output) throws IOException, NotBoundException {
		init(svcName, output);
	}
	
	/**
	 * private init function called by all Constructors 
	 * @param svcName service name
	 * @param output CrawlerOutput object to be used or null to read the output method from the property files
	 * @throws IOException e.g. when crawler.properties couldn't be read
	 * @throws NotBoundException if RMI output is used and the RMI connection couldn't be established
	 */
	private void init(String svcName, CrawlerOutput output) throws IOException, NotBoundException {
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
	
		if (output == null) {
			if(outputMethod.equals("SQL")){
				crawlerOutput=new SQLOutput(svcName, myProperties);
			}else if(outputMethod.equals("RMI")){
				crawlerOutput=new RMIOutput(svcName, myProperties);
			}
		}
		else crawlerOutput = output;
		

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

		apiKey = myProperties.getProperty("crawler.apiKey", apiKey);
		secKey = myProperties.getProperty("crawler.secKey", secKey);
		

		outputMethod = myProperties.getProperty("crawler.output",outputMethod);
	}
	
	/**
	 * infinite loop that calls the crawler-specific crawl function
	 */
	public void crawl() {
		BoundingBox curBox = null;
		while((curBox = nextSubBox(curBox)) != null) {
			crawlBox(curBox);
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
		return "outputMethod: "+outputMethod+", stopWords: "+showStopWord+", splitChars: "+splitChars;
	}
	
	private BoundingBox nextSubBox(BoundingBox curBox) {
		BoundingBox rc = null;
		double newX1, newY1, newX2, newY2;
		
		if (curBox == null) {
			newX1 = crawlArea.getX1();
			newY1 = crawlArea.getY1();
		}
		else {
			newX1 = curBox.getX2();
			newY1 = curBox.getY1();
			
			// check if we're at the right border
			if (newX1 + 0.0001 > crawlArea.getX2()) {
				// jump to the next line
				newX1 = crawlArea.getX1();
				newY1 = curBox.getY2();
			}
		}
		
		newX2 = Math.min(newX1+xOffset, crawlArea.getX2());
		newY2 = Math.min(newY1+yOffset, crawlArea.getY2());
		
		// return null if we're at the bottom of the crawlAreas
		if (newY1 +0.0001 < crawlArea.getY2()) {
			rc =  new BoundingBox(newX1, newY1, newX2, newY2);
		}
		return rc;
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
