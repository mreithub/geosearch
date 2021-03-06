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
	
	private int svcId;
	private String uid;
	private String link;
	private Timestamp validUntil;
	private Property[] properties;
	
	/**
	 * Constructor
	 *@param title Object title
	 *@param xPos Longitude
	 *@param yPos Latitude
	 *@param serviceID service id
	 *@param uniqueID the unique id 
	 *@param link object link (maximum length: 4096)
	 *@param valid_until time, when objects expire
	 *@param properties object properties array
	 *@param tags object tag array 
	 * */
	public DBGeoObject(String title, double xPos, double yPos, int serviceID, String uniqueID, String link, Timestamp valid_until, Property[] properties, String[] tags){
		this(0, title, xPos, yPos, serviceID, uniqueID, link, valid_until, properties, tags);
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
	public DBGeoObject(long id, String title, double xPos, double yPos, int serviceID, String uniqueID, String link, Timestamp validUntil, Property[] properties, String[] tags){
		super(id, title, xPos, yPos);
		svcId= serviceID;
		uid=uniqueID;
		this.link=link;
		this.validUntil=validUntil;
		this.properties=properties;
		setTags(tags);
	}

	/**
	 * Getter for service id
	 * @return service id 
	 * @deprecated use getSvcId() instead
	 */
	@Deprecated
	public int getSvc_id() {
		return svcId;
	}
	
	/**
	 * Getter for service ID
	 * @return service ID
	 */
	public int getSvcId() {
		return svcId;
	}
	
	/**
	 * changes the service id for this DBGeoObject
	 * @param svcID new service ID
	 */
	public void setSvcId(int svcID) {
		this.svcId = svcID;
	}

	/**
	 * Getter for object unique id
	 * @return object unique id
	 * */
	public String getUid() {
		return uid;
	}
	
	/**
	 * Setter for the GeoObject's unique ID 
	 * @param uid service-unique id
	 */
	public void setUid(String uid) {
		this.uid = uid; 
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
	 * @deprecated use getValidUntil instead
	 */
	@Deprecated
	public Timestamp getValid_until() {
		return validUntil;
	}
	
	/**
	 * Getter for object validity date
	 * @return object validity date
	 */
	public Timestamp getValidUntil() {
		return validUntil;
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
		if (properties != null)
		{
			for(int i=0;i<properties.length;i++){
				propStr+=i+".) "+properties[i].toString();
			}
		}
		return super.toString()+", title: "+ getTitle()+", svcId: "+svcId+", uid: "+uid+", link: "+link+", validUntil: "+validUntil+", properties: "+propStr;
	}
}
