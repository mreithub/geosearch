package at.fakeroot.sepm.shared.server;

import at.fakeroot.sepm.shared.client.serialize.GeoObject;

/**
 * @author Anca Cismasiu
 *
 */
public class DBGeoObject extends GeoObject {
	private String title;
	private int svc_id;
	private String uid;
	private String link;
	private String valid_until;
	private Property[] properties;
	
	public DBGeoObject(int objID, String titel, double xPos, double yPos, int serviceID, String uniqueID, String link, String valid_until, Property[] properties, String[] tags){
		super(objID, titel, xPos, yPos);
		svc_id= serviceID;
		uid=uniqueID;
		this.link=link;
		this.valid_until=valid_until;
		this.properties=properties;
		title = titel;
		setTags(tags);
	}

	public String getTitle() {
		return title;
	}

	public int getSvc_id() {
		return svc_id;
	}

	public String getUid() {
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
