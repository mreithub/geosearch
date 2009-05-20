package at.fakeroot.sepm.client;
import at.fakeroot.sepm.client.serialize.ClientGeoObject;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.overlay.Marker;

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
		super(clientGeoObject.getPoint());
		this.clientGeoObject = clientGeoObject;
		this.geoManager = geoManager;
		addMarkerClickHandler(this);
	}
	
	/**
	 * opens the detailview, as soon as the marker is clicked
	 */
	public void onClick(MarkerClickEvent event) 
	{
		//geoManager.showDetailView(this);
	}

	public ClientGeoObject getClientGeoObject() {
		return (clientGeoObject);
	}
}
