package at.fakeroot.sepm.client;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.event.MapClickHandler.MapClickEvent;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

import at.fakeroot.sepm.client.serialize.BoundingBox;
import at.fakeroot.sepm.client.serialize.ClientGeoObject;

public class GeoMap extends Composite
{
	private IGeoManager geoManager;
	private MapWidget geoMap;
	private Geocoder geoCoder;
	//TODO benötigt DetailView-Klasse.
	//private DetailView detailView;
	
	/**
	 * Constructor.
	 * @param geoManger: An object which implements the IGeoManager interface.
	 */
	public GeoMap(IGeoManager geoManager)
	{
		//Set the geoManager.
		this.geoManager = geoManager;
		
		//Create a panel which holds the geoMap.
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSize("100%", "100%");
		
		//Set the position of the geoMap so that it displays Austria.
		geoMap = new MapWidget(LatLng.newInstance(47.569114, 13.337402), 7);
		geoMap.setSize("100%", "100%");
		
		//Add the navigation control to the map.
		geoMap.addControl(new LargeMapControl());
		
		//Add the supported map types to the map.
		geoMap.addMapType(MapType.getNormalMap());
		geoMap.addMapType(MapType.getSatelliteMap());
		geoMap.addMapType(MapType.getPhysicalMap());
		geoMap.removeMapType(MapType.getHybridMap());
		geoMap.addControl(new MapTypeControl());
		
		//Set map behaviour.
		geoMap.setDoubleClickZoom(true);
		geoMap.setScrollWheelZoomEnabled(true);
		
		//Add the map to the panel.
		hPanel.add(geoMap);
		
		//Put the panel into a widget (required by GWT).
		initWidget(hPanel);
		
		//Create the geoCoder which is required to search for locations.
		geoCoder = new Geocoder();
	}
	
	/**
	 * Center the map at the given coordinates.
	 * @param x
	 * @param y
	 */
	public void setCenter(double x, double y)
	{
		geoMap.setCenter(LatLng.newInstance(x, y));
	}
	
	/**
	 * Searches for a given location and moves the displayed map region to the search result (if there
	 * is one).
	 * @param where: A string, specifying the location where to search.
	 * @return: The bounding box, containing the region displayed. If the search doesn't return
	 * any result, the region doesn't change.
	 */
	public BoundingBox search(String where)
	{
		geoCoder.getLatLng(where, new LatLngCallback()
			{
				public void onSuccess(LatLng point)
				{
					geoMap.setCenter(point);
				}
				
				public void onFailure()
				{
				}
			});
		LatLngBounds bounds = geoMap.getBounds();
		return(new BoundingBox(bounds.getSouthWest().getLatitude(), bounds.getNorthEast().getLongitude(),
			bounds.getNorthEast().getLatitude(), bounds.getSouthWest().getLongitude()));
	}
	
	/**
	 * Add a pin to the map.
	 * @param obj The ClientGeoObject which should be added.
	 */
	public void addPin(ClientGeoObject obj)
	{
		//TODO benötigt GeoPin-Klasse.
		
		/*GeoPin newPin = new GeoPin(geoManager, obj);
		geoMap.addOverlay(newPin);*/
	}
	
	/**
	 * Add mutliple pins to the map.
	 * @param objList The list of ClientGeoObjects which should be added.
	 */
	public void addPins(List<ClientGeoObject> objList)
	{
		Iterator<ClientGeoObject> iterator = objList.iterator();
		while (iterator.hasNext())
			addPin(iterator.next());
	}
	
	/**
	 * Set multiple pins on the map. Already existing pins are deleted.
	 * @param objList The list of ClientGeoObjects which should be added.
	 */
	public void setPins(List<ClientGeoObject> objList)
	{
		clearPins();
		addPins(objList);
	}
	
	/**
	 * Clear existing pins on the map.
	 */
	public void clearPins()
	{
		geoMap.clearOverlays();
	}
	
	//TODO benötigt GeoPin- und DetailView-Klasse.
	/*public DetailView createDetailView(GeoPin pin)
	{
		geoMap.getInfoWindow().open(pin, new InfoWindowContent("<b>Loading - please wait...</b>"));
		return (new DetailView(geoManager, pin.getGeoObj(), geoMap.getInfoWindow()));
	}*/
}
