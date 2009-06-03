

import java.io.Serializable;


/**
 * Contains the main parts of an GeoObject. 
 * @author JB
 *
 */
public class GeoObject implements Serializable {

	private int id;
	private String titel;
	private double xPos;
	private double yPos;
	private String tags[];
	
	/**
	 * StandardConstruction. Is required for the serializeation
	 */
	public GeoObject() {
	}

	/**
	 * 
	 * @param id Unique ID
	 * @param titel ObjTitel
	 * @param xPos (Lng=Longitute)
	 * @param yPos (Lat=Latitiute)
	 */
	public GeoObject(int id, String titel, double xPos, double yPos){
		this.id=id;
		this.titel=titel;
		this.xPos=xPos;
		this.yPos=yPos;
	}
	/**
	 * Returns the unique ID of the GeoObject
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the Title of the GeoObject
	 * @return
	 */
	public String getTitel() {
		return titel;
	}

	/**
	 * Returns the xPos (Lng=Longitute) of the GeoObject
	 * @return
	 */
	public double getXPos() {
		return xPos;
	}

	/**
	 * Returns the yPos (Lat=Latitute) of the GeoObject
	 * @return
	 */
	public double getYPos() {
		return yPos;
	}

	/**
	 * Returns the Positions of the GeoObject
	 * @return
	 */
	/*
	public LatLng getPoint(){
		return LatLng.newInstance(yPos, xPos);
	}
	*/
	
	/**
	 * Sets the uniqueID of the GeoObject
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the Title of the GeoObject
	 * @param titel
	 */
	public void setTitel(String titel) {
		this.titel = titel;
	}

	
	/**
	 * Sets the xPos (Lng=Longitute) of the GeoObject 
	 * @return
	 */
	public void setXPos(double pos) {
		xPos = pos;
	}

	/**
	 * Sets the yPos (Lat=Latitute) of the GeoObject 
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
		return "id: "+id+", titel: "+titel+", xPos(lng): "+xPos+", yPos(lat): "+yPos;
	}
	
}
