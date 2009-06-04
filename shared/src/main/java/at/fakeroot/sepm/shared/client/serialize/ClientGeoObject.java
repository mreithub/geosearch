package at.fakeroot.sepm.shared.client.serialize;
import java.io.Serializable;

/**
 * Contains GeoObject plus an Image and Tags to visualize it on the GUI. 
 * @author JB
 *
 */
public class ClientGeoObject extends GeoObject implements Serializable {
	String imageUrl;
	
	
	/**
	 * StandardConstruction. Is required for the serializeation
	 */
	public ClientGeoObject() {
	}

	/**
	 * @param id Unique ID
	 * @param titel ObjTitel
	 * @param xPos (Lng=Longitute)
	 * @param yPos (Lat=Latitiute)
	 * @param imageUrl Pin (Marker) Image
	 * @param tags GeoObject Tags
	 */
	public ClientGeoObject(int id, String titel, String imageUrl, String[] tags, double xPos, double yPos){
		super(id,titel,xPos,yPos);
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
