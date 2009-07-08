package at.fakeroot.sepm.crawler;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import org.apache.log4j.Logger;
import at.fakeroot.sepm.shared.server.DBGeoObject;
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
	
	/**
	 * Constructor
	 * @param svcName service name
	 * @throws IOException if the crawler's properties couldn't be read
	 * @throws NotBoundException if no RMI connection could be established
	 */
	public RMIOutput(String svcName, Properties props) throws IOException, NotBoundException {
		init(svcName, props);
	}
	
	/**
	 * private init function called by all Constructors 
	 * @param svcName service name
	 * @throws IOException e.g. when crawler.properties couldn't be read 
	 * @throws NotBoundException if no RMI connection could be established
	 */
	private void init(String svcName, Properties props) throws IOException, NotBoundException {
		logger.info("Crawler "+svcName+" started");

		loadProperties(props);

		// init RMI		
		Registry reg = LocateRegistry.getRegistry(rmiServer, rmiPort);
		for(int i=0;i<reg.list().length;i++){
			System.out.println("reg: "+reg.list()[i]);
		}

		//geoSaver = (IGeoSave) reg.lookup("rmi://"+rmiServer+":"+rmiPort+"/IGeoSave");
		geoSaver = (IGeoSave) reg.lookup("IGeoSave");
		logger.info("RMI connection opened on "+rmiServer+":"+rmiPort);

		// Request Service ID
		serviceID=requestServiceID(svcName);

	}

	private void loadProperties(Properties props) throws IOException {
		rmiServer = props.getProperty("rmi.server", rmiServer);
		rmiPort = Integer.parseInt(props.getProperty("rmi.port", Integer.toString(rmiPort)));
	}

	/**
	 * Send an array of DBGeoObjects to the Server
	 * @param newObjects Array of new Objects
	 */
	public void saveObjects(DBGeoObject[] newObjects) {
 		for(int i=0;i<newObjects.length;i++){
 			newObjects[i].setSvcId(serviceID);
		}
		
		try {
			geoSaver.saveObjects(newObjects);
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
