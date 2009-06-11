package at.fakeroot.sepm.shared.client.serialize;
import java.io.Serializable;

/**
 * Contains GeoObject plus an Image and Tags to visualize it on the GUI. 
 * @author JB
 *
 */
public class ClientGeoObject extends GeoObject implements Serializable {
	/// default serial version id
	private static final long serialVersionUID = 1L;
	String imageUrl;
	
	
	/**
	 * StandardConstruction. Is required for the serializeation
	 */
	public ClientGeoObject() {
	}

	/**
	 * Constructor
	 * @param title Object title
	 * @param xPos (Lng=Longitude)
	 * @param yPos (Lat=Latitude)
	 * @param imageUrl Pin (Marker) Image
	 * @param tags GeoObject Tags
	 */
	public ClientGeoObject(String title, String imageUrl, String[] tags, double xPos, double yPos){
		super(title,xPos,yPos);
		this.imageUrl=imageUrl;
		setTags(tags);
	}

	/**
	 * Constructor
	 * @param id object id
	 * @param title Object title
	 * @param xPos (Lng=Longitude)
	 * @param yPos (Lat=Latitude)
	 * @param imageUrl Pin (Marker) Image
	 * @param tags GeoObject Tags
	 */
	public ClientGeoObject(long id, String title, String imageUrl, String[] tags, double xPos, double yPos){
		super(id, title,xPos,yPos);
		this.imageUrl=imageUrl;
		setTags(tags);
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	
	public String toString() {
		String tags[] = getTags();
		String sTag="[";
		for(int i=0;i<tags.length;i++){
			sTag+=tags[i]+", ";
		}
		sTag+="]";
		return super.toString()+", imageUrl: "+imageUrl+", tags: "+sTag;
	}
}
