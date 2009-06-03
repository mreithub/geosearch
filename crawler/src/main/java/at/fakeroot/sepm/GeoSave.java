import java.rmi.Remote;
import java.rmi.RemoteException;


public interface GeoSave extends Remote {
	public void saveObject(DBGeoObject[] newGeoObj)throws RemoteException;
}
