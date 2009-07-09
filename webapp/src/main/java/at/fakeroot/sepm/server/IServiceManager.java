package at.fakeroot.sepm.server;

import java.io.IOException;
import java.sql.SQLException;

public interface IServiceManager {

	/**
	 * returns a DBService object with the Data from the 'service' table of the Database, selected
	 * by the svc_id, which is the parameter for this method
	 * @param svcId int ID of the service 
	 * @return DBService an object with all the information from the table 'service'
	 */
	public abstract DBService select(int svcId) throws SQLException,
			IOException;

	/**
	 * returns a DBService object with the Data from the 'service' table of the Database, selected
	 * by its name, which is the parameter for this method
	 * @param name String the name of the service
	 * @return DBService an object with all the information from the table 'service'
	 */
	public abstract DBService select(String name) throws SQLException,
			IOException;

}