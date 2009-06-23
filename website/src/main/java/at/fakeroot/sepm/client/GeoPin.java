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

	public ClientGeoObject getClientGeoObject() {
		return (clientGeoObject);
	}
	
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
