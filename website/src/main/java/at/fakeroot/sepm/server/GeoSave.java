package at.fakeroot.sepm.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import at.fakeroot.sepm.shared.server.DBGeoObject;
import at.fakeroot.sepm.shared.server.DBService;
import at.fakeroot.sepm.shared.server.GeoObjectManager;
import at.fakeroot.sepm.shared.server.IGeoSave;
import at.fakeroot.sepm.shared.server.ServiceManager;


public class GeoSave
{
	private static GeoSave instance;
	
	private GeoSave()
	{
		//--- JAVAInsel
		try {
			LocateRegistry.createRegistry( Registry.REGISTRY_PORT );
			//LocateRegistry.createRegistry(1525); 	    
		    
		    //GeoSave
		    IGeoSaveImpl geoSaver = new IGeoSaveImpl();
		    IGeoSave geoStub = (IGeoSave) UnicastRemoteObject.exportObject(geoSaver,0);
		    
		    Registry geoRegistry = LocateRegistry.getRegistry();
		    geoRegistry.rebind("IGeoSave", geoStub);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public static GeoSave getInstance()
	{
		if (instance == null)
			instance = new GeoSave();
		return(instance);
	}
	
}
