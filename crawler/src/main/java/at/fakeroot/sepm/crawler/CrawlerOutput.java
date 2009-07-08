package at.fakeroot.sepm.crawler;

import at.fakeroot.sepm.shared.server.DBGeoObject;

public interface CrawlerOutput {

	void saveObjects(DBGeoObject[] newObjects);
	
	String getSplitChars();
	
	String[] getStopWords();
}
