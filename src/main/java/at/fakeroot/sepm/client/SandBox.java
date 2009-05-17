package at.fakeroot.sepm.client;


import at.fakeroot.sepm.client.serialize.ClientGeoObject;
import at.fakeroot.sepm.client.serialize.GeoObject;
import at.fakeroot.sepm.client.serialize.SandBoxService;
import at.fakeroot.sepm.client.serialize.SandBoxServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.control.MenuMapTypeControl;
import com.google.gwt.maps.client.control.OverviewMapControl;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * TestClass
 * @author JB
 */
public class SandBox extends Composite {

	private final SandBoxServiceAsync sandBoxService = GWT.create(SandBoxService.class);
	
	VerticalPanel myVePa = new VerticalPanel();
	HorizontalPanel mapHoPa = new HorizontalPanel();
	VerticalPanel debugeConsole = new VerticalPanel();
	ScrollPanel myScroll = new ScrollPanel();
	
	public SandBox() {
		initWidget(myVePa);
		myVePa.add(mapHoPa);
		myScroll.setWidget(debugeConsole);
		myVePa.add(myScroll);
		myScroll.setHeight("100px");
		
	    
	    //Map machen
	    
	    final VerticalPanel markerVePa = new VerticalPanel();
	    markerVePa.add(new HTML("Bitte auf map Clicken"));
	    mapHoPa.setSize("100%", "300px");
	    mapHoPa.setBorderWidth(1);
	    
	    //Map Machen
	    final MapWidget myMap = new MapWidget();
	    myMap.setSize("500px", "300px");
	    mapHoPa.add(myMap);
	    myMap.addMapType(MapType.getEarthMap());
	    myMap.addMapType(MapType.getPhysicalMap());
	    myMap.addMapType(MapType.getMarsElevationMap());
	    myMap.addMapType(MapType.getSkyVisibleMap());
	    myMap.addControl(new LargeMapControl());
	    myMap.addControl(new MenuMapTypeControl());
	    //myMap.addControl(new OverviewMapControl());
	    myMap.setCenter(LatLng.newInstance(48.19538740833338, 16.34765625), 13);
	    
	    //Seite machen
	    mapHoPa.add(markerVePa);
	    markerVePa.setWidth("100%");
	    myMap.addMapClickHandler(new MapClickHandler(){
			public void onClick(MapClickEvent event) {
				final Marker tempMarker = new Marker(event.getLatLng());
				myMap.addOverlay(tempMarker);		
				
				HTML tempMyNews = new HTML(markerVePa.getWidgetCount()
						+".) Lat: "+event.getLatLng().getLatitude()
						+" Lng: "+event.getLatLng().getLongitude());
				
				markerVePa.add(tempMyNews);
				
				
				final InfoWindowContent myTempCont = new InfoWindowContent(
						new HTML("You Clicked: -----------------------------------------"+markerVePa.getWidgetCount()
								+".) <br />Lat: "+event.getLatLng().getLatitude()
								+" <br />Lng: "+event.getLatLng().getLongitude()+
								"<br /> <br /> <br /> <br /> <br /> <br />"+
								"<br /> <br /> <br /> <br /> <br /> <br />"));
				
				tempMyNews.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent arg0) {
						myMap.getInfoWindow().open(tempMarker.getLatLng(), myTempCont);		
					}
					
				});
				
				tempMarker.addMarkerClickHandler(new MarkerClickHandler(){
					public void onClick(MarkerClickEvent event) {
						myMap.getInfoWindow().open(event.getSender().getLatLng(), myTempCont);
					}
				});
			}
	    	
	    });
	    
	    
	    testGeoObject();
	}
	
	private void testGeoObject(){
		
		mapHoPa.add(new Button("RPCTest", new ClickHandler(){
			public void onClick(ClickEvent arg0) {
				System.out.println("rpc");	
				sandBoxService.getObjects(10, new AsyncCallback<ClientGeoObject[]>(){
					public void onFailure(Throwable arg0) {
						System.out.println("error");	
						debugeConsole.add(new HTML("error"));
					}

					public void onSuccess(ClientGeoObject[] result) {
						for(int i=0;i<result.length;i++){
							System.out.println(result[i]+" "+result[i].getPoint());
							debugeConsole.add(new HTML(result[i]+" "+result[i].getPoint()));
						}
						System.out.println("-------------------");
						debugeConsole.add(new HTML("-------------------"));
					}
					
				});
			}			
		}));
		
		GeoObject myGeoObject = new GeoObject();
		
		myGeoObject.setId(12);
		myGeoObject.setTitel("super Coole");
		myGeoObject.setXPos(45.15);
		myGeoObject.setYPos(12.15);
		
		System.out.println(myGeoObject);
		
	}
	
	
}
