package at.fakeroot.sepm.shared.server;

import java.sql.Timestamp;

import at.fakeroot.sepm.shared.client.serialize.GeoObject;

/**
 * Class representing a GeoObject with all the necessary properties to be stored in and read from the Database
 * @author Anca Cismasiu
 */
public class DBGeoObject extends GeoObject {
	/// default serial version id
	private static final long serialVersionUID = 1L;
	
	private int svc_id;
	private String uid;
	private String link;
	private Timestamp valid_until;
	private Property[] properties;
	
	/**
	 * Constructor
	 *@param title Object title
	 *@param xPos Longitude
	 *@param yPos Latitude
	 *@param serviceID service id
	 *@param uniqueID the unique id 
	 *@param link object link
	 *@param valid_until time, when objects expire
	 *@param properties object properties array
	 *@param tags object tag array 
	 * */
	public DBGeoObject(String title, double xPos, double yPos, int serviceID, String uniqueID, String link, Timestamp valid_until, Property[] properties, String[] tags){
		super(title, xPos, yPos);
		svc_id= serviceID;
		uid=uniqueID;
		this.link=link;
		this.valid_until=valid_until;
		this.properties=properties;
		setTags(tags);
	}
	
	/**
	 * Constructor
	 *@param id object id 
	 *@param title Object title
	 *@param xPos Longitude
	 *@param yPos Latitude
	 *@param serviceID service id
	 *@param uniqueID the unique id 
	 *@param link object link
	 *@param valid_until time, when objects expire
	 *@param properties object properties array
	 *@param tags object tag array 
	 * */
	public DBGeoObject(long id, String title, double xPos, double yPos, int serviceID, String uniqueID, String link, Timestamp valid_until, Property[] properties, String[] tags){
		super(id, title, xPos, yPos);
		svc_id= serviceID;
		uid=uniqueID;
		this.link=link;
		this.valid_until=valid_until;
		this.properties=properties;
		setTags(tags);
	}

	/**
	 * Getter for service id
	 * @return service id 
	 * */
	public int getSvc_id() {
		return svc_id;
	}

	/**
	 * Getter for object unique id
	 * @return object unique id
	 * */
	public String getUid() {
		return uid;
	}

	/**
	 * Getter for object link
	 * @return object link
	 * */
	public String getLink() {
		return link;
	}

	/**
	 * Getter for object validity date
	 * @return object validity date
	 * */
	public Timestamp getValid_until() {
		return valid_until;
	}

	/**
	 * Getter for object properties
	 * @return object properties
	 * */
	public Property[] getProperties() {
		return properties;
	}
	
	public String toString() {
		String propStr="";
		for(int i=0;i<properties.length;i++){
			propStr+=i+".) "+properties[i].toString();
		}
		return super.toString()+", title: "+ getTitle()+", svc_id: "+svc_id+", uid: "+uid+", link: "+link+", valid_until: "+valid_until+", properties: "+propStr;
	}
}
