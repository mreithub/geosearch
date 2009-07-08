package at.fakeroot.sepm.crawler;

import java.io.IOException;

import at.fakeroot.sepm.shared.server.DBGeoObject;

public interface CrawlerOutput {

	void loadProperties(String filename) throws IOException;
	
	void saveObjects(DBGeoObject[] newObjects);
	
	//int getSvcID();
	
	String getSplitChars();
	
	String[] getStopWords();
}
