package at.fakeroot.sepm.client;


import java.util.ArrayList;

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
	
	private String myWhere="";
	private String myWhat="";
	private BoundingBox myBound;
	ArrayList<ClientGeoObject> testList = new ArrayList<ClientGeoObject>();
	
	public GeoManager() {
		this.geoMap=new GeoMap(this);
		this.searchBox=new SearchBox(this);
		this.tagCloud = new TagCloud(this);
		
		//test sachen eben
		addSearchTag("testtag");
		
		
	}
	
	public void drawGUI(){
		PopupPanel searchPop = new PopupPanel(false);
		searchPop.setWidget(this.searchBox);
		searchPop.show();
		searchPop.setPopupPosition(5, 5);
		
		PopupPanel tagPop = new PopupPanel(false);
		tagPop.setWidget(tagCloud);
		tagPop.setPopupPosition(5, 80);
		tagPop.show();
	}
	
	public void addSearchTag(String tag) {
		this.searchBox.setWhat(searchBox.getWhat()+" "+tag);
		
	}

	public void search(String where, String what) {
		System.out.println("searchByWhereAndWhat");
		myWhat=what;
		geoMap.search(where);		
	}

	public void search(String what) {
		System.out.println("searchByWhat: "+what);
		
		//Hier beginnt echte suche
		//Anfrage an DB mit Where and What
		
		int randTag=(int)(Math.random()*10);
		String[] tempTagString = new String[randTag];
		for(int i=0;i<randTag;i++){
			tempTagString[i]="tag"+i;
		}
		
		testList.add(new ClientGeoObject(1,"SuperTuper","photo.png",tempTagString,
				myBound.getCenter().getLatitude(),
				myBound.getCenter().getLongitude()));
		geoMap.setPins(testList);
		
		
		tagCloud.refresh(testList.iterator());
		
	}

	public void setBoundingBox(BoundingBox box) {
		System.out.println("setBoundingBox: "+box.toString());
		myBound=box;
		search(myWhat);
	}

	public void showDetailView(ClientGeoObject geoObject) {
		System.out.println("showDetailView");
		
			geoMap.createDetailView(geoObject);
		///TODO benotigt ObjectDetail		
			//detailView.setDetail(....);
	}
	
	public GeoMap getGeoMap(){
		return geoMap;
	}

}
