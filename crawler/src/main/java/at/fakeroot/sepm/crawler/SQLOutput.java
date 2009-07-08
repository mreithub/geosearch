package at.fakeroot.sepm.crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import at.fakeroot.sepm.shared.server.DBGeoObject;

public class SQLOutput implements CrawlerOutput {
	private static final Logger logger = Logger.getRootLogger();
	private int serviceID=23;
	private String fileName="objects.sql";
	private FileWriter writer;
	private File file;
	private String splitChars=" \r\n.,!?\"`´°\\\'";
	private String[] stopWords=new String[]{"der", "die", "das", "in", "ein", "aus", "für"};
	
	public SQLOutput(String svcName) throws IOException {
		init(svcName);
	}
	
	/**
	 * private init function called by all Constructors 
	 * @param svcName service name
	 * @throws IOException e.g. when crawler.properties couldn't be read 
	 */
	private void init(String svcName) throws IOException {
		logger.info("Crawler "+svcName+" started");
		// specific properties overwrite the global ones
		loadProperties("SQLOutput.properties");
		try {
			loadProperties(svcName+".SQLOutput.properties");
		}
		catch (IOException e) {
			logger.info("Couldn't open SQLOutput.properties", e);
		}

		file = new File(fileName);
		writer = new FileWriter(file ,true);
		
		//writer.close();
		
	}

	@Override
	public String getSplitChars() {
		return splitChars;
	}

	@Override
	public String[] getStopWords() {
		return stopWords;
	}


	@Override
	public void loadProperties(String filename) throws IOException {
		Properties prop = new Properties();
		InputStream propStream = getClass().getResourceAsStream("/"+filename);
		
		if (propStream == null) {
			logger.error("Error: Couldn't open property file '"+filename+"'");
			throw new IOException("Error: Couldn't open property file '"+filename+"'");
		}
		prop.load(propStream);

		
		//serviceID = prop.getProperty("sql.SvcId", serviceID);
		serviceID = Integer.parseInt(prop.getProperty("sql.SvcId", Integer.toString(serviceID)));
		splitChars = prop.getProperty("sql.SplitChars",splitChars);
		
		String stopWordsTest=null;
		String stopWordsString = prop.getProperty("sql.StopWords", stopWordsTest);
		
		if(stopWordsString!=null){
			stopWords=stopWordsString.split(",");
		}
	}

	@Override
	public void saveObjects(DBGeoObject[] newObjects) {
		System.err.println("GeoSave.saveObjects ("+newObjects.length+")");
		for (int i = 0; i < newObjects.length; i++) {
			System.err.println("GeoSave.saveObjects ("+newObjects[i].getSvc_id()+")");
			
			try {
				writer.write("INSER INTO XXX (" +
						"%servicename_id%) VALUES (" +
						newObjects[i].getTitle()+");");
				writer.write(System.getProperty("line.separator"));
				writer.flush();
			} catch (IOException e) {
				logger.error("Error: Couldn't write SQL file");
			}
			
		}
		
	}

}
