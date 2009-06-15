package at.fakeroot.sepm.server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import at.fakeroot.sepm.shared.server.DBGeoObject;
import at.fakeroot.sepm.shared.server.DBService;
import at.fakeroot.sepm.shared.server.GeoObjectManager;
import at.fakeroot.sepm.shared.server.IGeoSave;
import at.fakeroot.sepm.shared.server.ServiceManager;


public class GeoSave extends HttpServlet
{
	// default serial version id
	private static final long serialVersionUID = 1L;
	private static GeoSave instance;
	
	public GeoSave()
	{
		//--- JAVAInsel
		try {
			LocateRegistry.createRegistry(38492);
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
	
/*	public void init(ServletConfig config) throws ServletException {
		getInstance();
	}*/
	
	public static GeoSave getInstance()
	{
		if (instance == null)
			instance = new GeoSave();
		return(instance);
	}	
}
