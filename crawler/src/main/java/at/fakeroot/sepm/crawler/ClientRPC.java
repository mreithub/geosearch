package at.fakeroot.sepm.crawler;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;




public class ClientRPC {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws  RemoteException, NotBoundException   {
		System.out.println("clientRPC");
		
		/*
		Registry registry = LocateRegistry.getRegistry("10.0.0.150"); 		
	    
	    GeoSave geoSaver = (GeoSave) registry.lookup("GeoSave");
	    String[] tabs= {"tag1","tag2"};
	    Property[] myProperty = new Property[1];
	    myProperty[0]= new Property("proName","proValue");
	    
	    DBGeoObject[] tempDBGeoObject = new DBGeoObject[5];
	    for(int i=0; i<5;i++){
	    	tempDBGeoObject[i]=new DBGeoObject(12+i,"Client1",1.1,2.2,12,"uniqu","link","valid",myProperty,tabs);
		    
		    //System.out.println("OK?: "+geoSaver.saveObject(tempDBGeoObject));
	    }
	    geoSaver.saveObject(tempDBGeoObject);
	    */
	    new LocrCrawler();
	}
	

}



