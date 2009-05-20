package at.fakeroot.sepm.client;


import java.util.ArrayList;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;

import at.fakeroot.sepm.client.serialize.BoundingBox;
import at.fakeroot.sepm.client.serialize.ClientGeoObject;

/**
 * Manage all requests between the Objects and the server.
 * @author JB
 *
 */
public class GeoManager implements IGeoManager {
	private SearchBox searchBox;
	//private ResultInfoBox resultBox;
	private TagCloud tagCloud;
	private GeoMap geoMap;
	private ArrayList<ClientGeoObject> geoObjects;
	
	public GeoManager() {
		this.geoMap=new GeoMap(this);
		this.searchBox=new SearchBox(this);
		tagCloud = new TagCloud(this);
	}
	
	public void drawGUI(){
		PopupPanel searchPop = new PopupPanel(false);
		searchPop.setWidget(this.searchBox);
		searchPop.show();
		searchPop.setPopupPosition(50, 50);
		
		PopupPanel tagPop = new PopupPanel(false);
		tagPop.setTitle("TagCloud");
		tagPop.setWidget(tagCloud);
		tagPop.setPopupPosition(50, 120);
		tagPop.show();
	}
	
	public void addSearchTag(String tag) {
		// TODO Auto-generated method stub
		
	}

	public void search(String where, String what) {
		BoundingBox whereBound = geoMap.search(where);
		
		
	}

	public void search(String what) {
		// TODO Auto-generated method stub
		
	}

	public void setBoundingBox(BoundingBox box) {
		System.out.println("bound: "+box.toString());
		
	}

	public void showDetailView(ClientGeoObject geoObject) {
		// TODO Auto-generated method stub
		
	}
	
	public GeoMap getGeoMap(){
		return geoMap;
	}

}
