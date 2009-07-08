package at.fakeroot.sepm.crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import at.fakeroot.sepm.shared.server.DBGeoObject;

public class SQLOutput implements CrawlerOutput {
	private static final Logger logger = Logger.getRootLogger();
	private String outputFileName="objects.sql";
	private FileWriter writer;
	private File file;
	private String splitChars=" \r\n.,!?\"`´°\\\'";
	private String[] stopWords=new String[]{"der", "die", "das", "in", "ein", "aus", "für"};
	
	/**
	 * constructor
	 * @param svcName service Name
	 * @param props init Properties 
	 * @throws IOException if the output file couldn't be opened
	 */
	public SQLOutput(String svcName, Properties props) throws IOException {
		init(svcName, props);
	}
	
	/**
	 * private init function called by all Constructors 
	 * @param svcName service name
	 * @throws IOException if the output file couldn't be opened
	 */
	private void init(String svcName, Properties props) throws IOException {
		logger.info("Crawler "+svcName+" started");
		// specific properties overwrite the global ones
		loadProperties(props);

		file = new File(outputFileName);
		writer = new FileWriter(file ,false);
	}

	@Override
	public String getSplitChars() {
		return splitChars;
	}

	@Override
	public String[] getStopWords() {
		return stopWords;
	}


	private void loadProperties(Properties props) {
		outputFileName = props.getProperty("sql.filename", outputFileName);
		splitChars = props.getProperty("sql.SplitChars",splitChars);
		
		String stopWordsString = props.getProperty("sql.StopWords");
		
		if(stopWordsString!=null){
			stopWords=stopWordsString.split(",");
		}
	}

	@Override
	public void saveObjects(DBGeoObject[] newObjects) {
		System.err.println("GeoSave.saveObjects ("+newObjects.length+")");
		for (int i = 0; i < newObjects.length; i++) {
			System.err.println("GeoSave.saveObjects ("+newObjects[i].getSvcId()+")");
			
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
