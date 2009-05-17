package at.fakeroot.sepm.client;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

import at.fakeroot.sepm.client.serialize.BoundingBox;
import at.fakeroot.sepm.client.serialize.ClientGeoObject;

public class GeoMap extends Composite
{
	private IGeoManager geoManager;
	private MapWidget geoMap;
	private Geocoder geoCoder;
	//todo
	//private DetailView detailView;
	
	//public GeoMap(IGeoManager geoManager)
	public GeoMap()
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
		
		//Add the map to the panel.
		hPanel.add(geoMap);
		initWidget(hPanel);
		
		//Create the geoCoder.
		geoCoder = new Geocoder();
	}
	
	public void setCenter(double x, double y)
	{
		geoMap.setCenter(LatLng.newInstance(x, y));
	}
	
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
	
	public void addPin(ClientGeoObject obj)
	{
	}
	
	public void addPins(List<ClientGeoObject> objList)
	{
		Iterator<ClientGeoObject> iterator = objList.iterator();
		while (iterator.hasNext())
			addPin(iterator.next());
	}
	
	public void setPins(List<ClientGeoObject> objList)
	{
		clearPins();
		addPins(objList);
	}
	
	public void clearPins()
	{
	}
	
	//todo
	/*public DetailView createDetailView(ClientGeoObject obj)
	{
		
	}*/
}
