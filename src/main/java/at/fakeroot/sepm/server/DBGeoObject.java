package at.fakeroot.sepm.server;

import at.fakeroot.sepm.client.serialize.GeoObject;

/**
 * @author Anca Cismasiu
 *
 */
public class DBGeoObject extends GeoObject {

	private int svc_id;
	private int uid;
	private String link;
	private String valid_until;
	private Property[] properties;
	
	public DBGeoObject(int serviceID, int uniqueID, String link, String valid_until, Property[] properties, String[] tags){
		svc_id= serviceID;
		uid=uniqueID;
		this.link=link;
		this.valid_until=valid_until;
		this.properties=properties;
		setTags(tags);
	}

	public int getSvc_id() {
		return svc_id;
	}

	public int getUid() {
		return uid;
	}

	public String getLink() {
		return link;
	}

	public String getValid_until() {
		return valid_until;
	}

	public Property[] getProperties() {
		return properties;
	}
}
