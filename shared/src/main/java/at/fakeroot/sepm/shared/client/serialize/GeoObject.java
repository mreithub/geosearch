package at.fakeroot.sepm.shared.client.serialize;

import java.io.Serializable;

import com.google.gwt.maps.client.geom.LatLng;

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
		this.title=title;
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
		this.title=title;
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
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
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
	}

	public String toString() {
		return "id: "+id+", title: "+title+", xPos(lng): "+xPos+", yPos(lat): "+yPos;
	}
	
}
