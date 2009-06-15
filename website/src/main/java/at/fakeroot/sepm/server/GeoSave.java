package at.fakeroot.sepm.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import at.fakeroot.sepm.shared.server.IGeoSave;


public class GeoSave extends HttpServlet
{
	// default serial version id
	private static final long serialVersionUID = 1L;
	private static Registry registry = null;
	private static IGeoSave geoSaver;
	private static IGeoSave geoStub;
	
	public GeoSave() throws ServletException
	{
	}
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		//--- JAVAInsel
		try {
			try {
				registry = LocateRegistry.createRegistry(38492);
			}
			catch (RemoteException e) {
				registry = LocateRegistry.getRegistry(38492);
			}

		    //GeoSave
		    geoSaver = new IGeoSaveImpl();
		    geoStub = (IGeoSave) UnicastRemoteObject.exportObject(geoSaver, 0);
		    
		    registry.rebind("IGeoSave", geoStub);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			throw new ServletException("Couldn't init RMI server", e);
		}
	}
	
	public void destroy() {
		if (registry != null) {
			try {
				registry.unbind("IGeoSave");
				UnicastRemoteObject.unexportObject(registry,true);
				registry = null;
			} catch (Exception e) {
				Logger.getRootLogger().error("Failed to unbind registry 'IGeoSave'", e);
			}
		}
	}
}
