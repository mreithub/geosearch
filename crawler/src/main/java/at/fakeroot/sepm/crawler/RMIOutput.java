package at.fakeroot.sepm.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import org.apache.log4j.Logger;
import at.fakeroot.sepm.shared.server.DBGeoObject;
import at.fakeroot.sepm.shared.server.DBGeoObjectTest;
import at.fakeroot.sepm.shared.server.IGeoSave;

public class RMIOutput implements CrawlerOutput {
	private static final Logger logger = Logger.getRootLogger();
	private IGeoSave geoSaver;
	private int serviceID;


	/**
	 * RMI server
	 */
	private String rmiServer = "localhost";
	
	/**
	 * RMI port
	 */
	private int rmiPort = 1099;
	
	public RMIOutput(String svcName) throws IOException {
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
		loadProperties("RMIOutput.properties");
		try {
			loadProperties(svcName+".RMIOutput.properties");
		}
		catch (IOException e) {
			logger.info("Couldn't open "+svcName+".RMIOutput.properties", e);
		}

		// init RMI		
		Registry reg = LocateRegistry.getRegistry(rmiServer, rmiPort);
		for(int i=0;i<reg.list().length;i++){
			System.out.println("reg: "+reg.list()[i]);
		}
		try {
			//geoSaver = (IGeoSave) reg.lookup("rmi://"+rmiServer+":"+rmiPort+"/IGeoSave");
			geoSaver = (IGeoSave) reg.lookup("IGeoSave");
			logger.info("RMI connection opened on "+rmiServer+":"+rmiPort);
		}
		catch (Exception e) {
			// if we can't get a RMI connection, enter testing mode
			geoSaver = new GeoSaveTest();
			logger.error("RMI connection failt on "+rmiServer+":"+rmiPort,e);
		}
		

		// Request Service ID
		//System.out.println("id: "+requestServiceID(svcName));
		serviceID=requestServiceID(svcName);

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

		
		rmiServer = prop.getProperty("rmi.server", rmiServer);
		rmiPort = Integer.parseInt(prop.getProperty("rmi.port", Integer.toString(rmiPort)));

		
	}

	/**
	 * Send an array of DBGeoObjects to the Server
	 * @param newObjects Array of new Objects
	 */
	public void saveObjects(DBGeoObject[] newObjects) {
		DBGeoObject[] saveObjects = new DBGeoObject[newObjects.length];
 		for(int i=0;i<newObjects.length;i++){
 			saveObjects[i]=new DBGeoObject(
 					newObjects[i].getId(),
 					newObjects[i].getTitle(),
 					newObjects[i].getXPos(),
 					newObjects[i].getYPos(),
 					serviceID,
 					newObjects[i].getUid(),
 					newObjects[i].getLink(),
 					newObjects[i].getValid_until(),
 					newObjects[i].getProperties(),
 					newObjects[i].getTags());
		}
		
		try {
			geoSaver.saveObjects(saveObjects);
		} catch (RemoteException e) {
			logger.error("Error: Saving Objects",e);
			e.printStackTrace();
		} 		
	}	
	
	/**
	 * get the Crawler's service ID from the IGeoSave RMI server 
	 * @param svcName unique service name 
	 * @return service ID
	 */
	private int requestServiceID(String svcName) {
		int rc;
		try {
			rc = geoSaver.getServiceID(svcName);
		} catch (RemoteException e) {
			logger.error("Error: No ServiceId",e);
			e.printStackTrace();
			return -1;
		}

		return rc;
	}


	/**
	 * get the stop word list from the IGeoSave RMI server
	 * @return String array filled with stop words
	 */
	public String[] getStopWords(){
		String[] rc;
		try{
			rc = geoSaver.getStopWords();
		}catch (RemoteException e) {
			logger.error("Error: No StopWords",e);
			e.printStackTrace();
			return null;
		}
		return rc;
	}
	
	/**
	 * get the list of word separating characters from the IGeoSave interface
	 * @return
	 */
	public String getSplitChars(){
		String rc;
		try{
			rc = geoSaver.getSplitChars();
		}catch (RemoteException e) {
			logger.error("Error: No SplitChars",e);
			e.printStackTrace();
			return null;
		}
		
		return rc;
	}
	
	
	
}
