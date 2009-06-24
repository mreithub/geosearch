package at.fakeroot.sepm.shared.client.serialize;

import java.io.Serializable;

/**
 * Contains the main parts of an GeoObject. 
 * @author JB
 *
 */
public class GeoObject implements Serializable {
	/// default serial version id
	private static final long serialVersionUID = 1L;
	private long id=0;
	private String title;
	private double xPos;
	private double yPos;
	private String tags[];
	
	/**
	 * Standard Constructor. Is required for the serialization.
	 */
	public GeoObject() {
	}

	/**
	 * Constructor
	 * @param id Object ID
	 * @param title Object title
	 * @param xPos (Lng=Longitude)
	 * @param yPos (Lat=Latitude)
	 */
	public GeoObject(long id, String title, double xPos, double yPos){
		this.id=id;
		this.title=truncate(title, 255);
		this.xPos=xPos;
		this.yPos=yPos;
	}
	
	/**
	 * Constructor
	 * @param title Object title
	 * @param xPos (Lng=Longitude)
	 * @param yPos (Lat=Latitude)
	 */
	public GeoObject(String title, double xPos, double yPos){
		this.title=truncate(title, 255);
		this.xPos=xPos;
		this.yPos=yPos;
	}
	
	
	/**
	 * Returns the unique ID of the GeoObject
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * Returns the Title of the GeoObject
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the xPos (Lng=Longitude) of the GeoObject
	 * @return
	 */
	public double getXPos() {
		return xPos;
	}

	/**
	 * Returns the yPos (Lat=Latitude) of the GeoObject
	 * @return
	 */
	public double getYPos() {
		return yPos;
	}

	
	
	/**
	 * Sets the uniqueID of the GeoObject
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Sets the Title of the GeoObject
	 * @param title title of the GeoObject, will be truncated to 255 characters
	 */
	public void setTitle(String title) {
		this.title = truncate(title, 255);
	}

	
	/**
	 * Sets the xPos (Lng=Longitude) of the GeoObject 
	 * @return
	 */
	public void setXPos(double pos) {
		xPos = pos;
	}

	/**
	 * Sets the yPos (Lat=Latitude) of the GeoObject 
	 * @return
	 */
	public void setYPos(double pos) {
		yPos = pos;
	}
	
	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
		
		// truncate the tags
		for (int i = 0; i < tags.length; i++) {
			if (tags[i].length() > 255) tags[i] = truncate(tags[i], 255);
		}
	}

	public String toString() {
		String strTags="";
		for(int i=0;i<tags.length;i++){
			strTags+=tags[i]+", ";
		}
		return "id: "+id+", title: "+title+", xPos(lng): "+xPos+", yPos(lat): "+yPos+", Tags: "+strTags;
	}
	
	/**
	 * truncate a String if necessary (and append '...' to it)
	 * @param str String to truncate after len characters
	 * @param len maximum String length
	 * @return truncated String
	 */
	public static String truncate(String str, int len) {
		String rc;
		
		if (str.length() > len) rc = str.substring(0, len-3)+"...";
		else rc = str;
		
		return rc;
	}
	
}
