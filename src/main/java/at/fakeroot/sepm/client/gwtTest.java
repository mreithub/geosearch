package at.fakeroot.sepm.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.control.MenuMapTypeControl;
import com.google.gwt.maps.client.control.OverviewMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class gwtTest implements EntryPoint {

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    Image img = new Image("http://code.google.com/webtoolkit/logo-185x175.png");
    Button button = new Button("Click me");

    VerticalPanel vPanel = new VerticalPanel();
    // We can add style names.
    vPanel.addStyleName("widePanel");
    vPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
    vPanel.add(img);
    vPanel.add(button);

    // Add image and button to the RootPanel
    RootPanel.get().add(vPanel);

    // Create the dialog box
    final DialogBox dialogBox = new DialogBox();
    dialogBox.setText("Welcome to GWT!");
    dialogBox.setAnimationEnabled(true);
    Button closeButton = new Button("close");
    VerticalPanel dialogVPanel = new VerticalPanel();
    dialogVPanel.setWidth("100%");
    dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
    dialogVPanel.add(closeButton);

    closeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        dialogBox.hide();
      }
    });

    // Set the contents of the Widget
    dialogBox.setWidget(dialogVPanel);
    
    button.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        dialogBox.center();
        dialogBox.show();
      }
    });
    
    
    
    //Map machen
    HorizontalPanel mapHoPa = new HorizontalPanel();
    final VerticalPanel markerVePa = new VerticalPanel();
    markerVePa.add(new HTML("Bitte auf map Clicken"));
    mapHoPa.setSize("100%", "500px");
    mapHoPa.setBorderWidth(1);
    RootPanel.get("mapDiv").add(mapHoPa);
    
    //Map Machen
    final MapWidget myMap = new MapWidget();
    myMap.setSize("500px", "500px");
    mapHoPa.add(myMap);
    myMap.addMapType(MapType.getEarthMap());
    myMap.addMapType(MapType.getPhysicalMap());
    myMap.addMapType(MapType.getMarsElevationMap());
    myMap.addMapType(MapType.getSkyVisibleMap());
    myMap.addControl(new LargeMapControl());
    myMap.addControl(new MenuMapTypeControl());
    myMap.addControl(new OverviewMapControl());
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
					new HTML("Du klickst: -----------------------------------------"+markerVePa.getWidgetCount()
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
    
 }
}
