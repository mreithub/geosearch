package at.fakeroot.sepm.client;
import at.fakeroot.sepm.shared.client.serialize.ClientGeoObject;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;

/**
 * The class GeoPin represents a marker on a Map
 * @author RK
 */
public class GeoPin extends Marker implements MarkerClickHandler
{
	private ClientGeoObject clientGeoObject;
	private IGeoManager geoManager;
	
	/**
	 * Constructor
	 * @param geoManager an Object, that implements the Interface IGeoManager
	 * @param clientGeoObject is needed to identify the Marker
	 */
	
	public GeoPin(IGeoManager geoManager, ClientGeoObject clientGeoObject)
	{
		super(clientGeoObject.getPoint(),  markerOpt(clientGeoObject));
		this.clientGeoObject = clientGeoObject;
		this.geoManager = geoManager;
		addMarkerClickHandler(this);
	}
	
	/**
	 * opens the DetailView, as soon as the marker is clicked
	 */
	public void onClick(MarkerClickEvent event) 
	{
		geoManager.showDetailView(clientGeoObject);
	}

	/**
	 * Getter for the ClientGeoObject, which is represented by the Pin
	 * @return ClientGeoObject
	 */
	public ClientGeoObject getClientGeoObject() {
		return (clientGeoObject);
	}
	
	/**
	 * A static method, which sets the right MarkerOptions (the Icon for the Pin, the Title of the Pin).
	 * It is needed for the super() call in the Constructor (all values must be set, before invoking the
	 * super constructor)
	 * @param cgo ClientGeoObject which will be represented by the Pin
	 * @return MarkerOptions an object that contains all settings for the Marker
	 */
	private static MarkerOptions markerOpt(ClientGeoObject cgo)
	{
		//MarkerOptions
		Icon markerIcon = Icon.newInstance(cgo.getImageUrl());
		markerIcon.setIconAnchor(Point.newInstance(13, 40));
		MarkerOptions markerOptions = MarkerOptions.newInstance(markerIcon);
		markerOptions.setTitle(cgo.getTitle());
		return markerOptions;
	}
	
}
