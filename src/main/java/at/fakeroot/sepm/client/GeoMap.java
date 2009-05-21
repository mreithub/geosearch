package at.fakeroot.sepm.client;

import java.util.Iterator;
import java.util.List;

import at.fakeroot.sepm.client.serialize.BoundingBox;
import at.fakeroot.sepm.client.serialize.ClientGeoObject;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.event.MapDragEndHandler;
import com.google.gwt.maps.client.event.MapZoomEndHandler;
import com.google.gwt.maps.client.geocode.GeoAddressAccuracy;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class GeoMap extends Composite implements MapDragEndHandler, MapZoomEndHandler
{
	private IGeoManager geoManager;
	private MapWidget geoMap;
	private Geocoder geoCoder;
	//TODO benötigt DetailView-Klasse.
	//private DetailView detailView;
	
	/**
	 * Constructor.
	 * @param geoManger An object which implements the IGeoManager interface.
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
		
		//Add DragEndHandler
		geoMap.addMapDragEndHandler(this);		
		
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
	 * @param where specifies the location where to search.
	 */
	public void search(String where)
	{
		geoCoder.getLocations(where, new LocationCallback()
			{	
				public void onSuccess(JsArray<Placemark> locations)
				{
					if (locations.length() == 0)
						return;
					geoMap.setCenter(locations.get(0).getPoint());
					int resultType = locations.get(0).getAccuracy();
					if (resultType == GeoAddressAccuracy.COUNTRY)
						geoMap.setZoomLevel(6);
					else if (resultType == GeoAddressAccuracy.REGION)
						geoMap.setZoomLevel(8);
					else if (resultType == GeoAddressAccuracy.SUB_REGION)
						geoMap.setZoomLevel(9);
					else if (resultType == GeoAddressAccuracy.TOWN)
						geoMap.setZoomLevel(11);
					else if (resultType == GeoAddressAccuracy.POSTAL_CODE)
						geoMap.setZoomLevel(12);
					else if (resultType == GeoAddressAccuracy.STREET)
						geoMap.setZoomLevel(13);
					else if (resultType == GeoAddressAccuracy.INTERSECTION)
						geoMap.setZoomLevel(15);
					else if (resultType == GeoAddressAccuracy.ADDRESS)
						geoMap.setZoomLevel(16);
				}
				
				public void onFailure(int statusCode)
				{
				}
			});
				
		//Send the new BoundingBox to the GeoManager.
		geoManager.setBoundingBox(getBoundingBox());
	}
	
	/**
	 * Add a pin to the map.
	 * @param obj The ClientGeoObject which should be added.
	 */
	public void addPin(ClientGeoObject obj)
	{
		GeoPin newPin = new GeoPin(geoManager, obj);
		geoMap.addOverlay(newPin);
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

	/**
	 * This function is called when a map drag event ends. The bounding box of the GeoManager has
	 * to be updated.
	 * @param event The MapDragEndEvent.
	 */
	public void onDragEnd(MapDragEndEvent event)
	{
		geoManager.setBoundingBox(getBoundingBox());
	}

	/**
	 * This function is called when a map zoom event ends. The bounding box of the GeoManager has
	 * to be updated.
	 * @param event The MapZoomEndEvent.
	 */
	public void onZoomEnd(MapZoomEndEvent event)
	{
		geoManager.setBoundingBox(getBoundingBox());
	}
	
	/**
	 * This function returns the bounding box which represents the currently displayed region within the map.
	 * @return The bounding box, containing the coordinates.
	 */
	private BoundingBox getBoundingBox()
	{
		return(new BoundingBox(
			geoMap.getBounds().getSouthWest().getLatitude(),
			geoMap.getBounds().getNorthEast().getLongitude(),
			geoMap.getBounds().getNorthEast().getLatitude(),
			geoMap.getBounds().getSouthWest().getLongitude()));
	}
	
	//TODO benötigt GeoPin- und DetailView-Klasse.
	/*public DetailView createDetailView(GeoPin pin)
	{
		geoMap.getInfoWindow().open(pin, new InfoWindowContent("<b>Loading - please wait...</b>"));
		return (new DetailView(geoManager, pin.getGeoObj(), geoMap.getInfoWindow()));
	}*/
}
