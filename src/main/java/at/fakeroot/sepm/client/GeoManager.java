package at.fakeroot.sepm.client;


import java.util.ArrayList;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import at.fakeroot.sepm.client.serialize.BoundingBox;
import at.fakeroot.sepm.client.serialize.ClientGeoObject;

/**
 * Manage all requests between the Objects and the server.
 * @author JB
 *
 */
public class GeoManager implements IGeoManager {
	private int XOFFSET=5;
	private int YOFFSET=5;
	private Image logo = new Image("images/design/logo_no_shaddow.png");
	private SearchBox searchBox;
	private ResultInfoBox resultBox;
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
		this.resultBox = new ResultInfoBox();
		
		//test sachen eben
		addSearchTag("testtag");
		
		
	}
	
	public void drawGUI(){
		//Logo
		PopupPanel logoPop = new PopupPanel(false);
		logo.setPixelSize(190, 40);
		logoPop.setWidget(this.logo);
		logoPop.show();
		logoPop.setPopupPosition(XOFFSET, YOFFSET);
		
		//SearchBox
		PopupPanel searchPop = new PopupPanel(false);
		searchPop.setWidget(this.searchBox);
		searchPop.setPopupPosition(XOFFSET, YOFFSET+60);
		searchPop.show();		
		
		//ResultBox
		PopupPanel infoPop = new PopupPanel(false);
		infoPop.setWidget(resultBox);
		infoPop.setPopupPosition(XOFFSET, YOFFSET+130);
		infoPop.show();
		
		//TagCloude
		PopupPanel tagPop = new PopupPanel(false);
		tagPop.setWidget(tagCloud);
		tagPop.setPopupPosition(5, 230);
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
		
		resultBox.refresh(randTag, randTag+10);
		
	}

	public void setBoundingBox(BoundingBox box) {
		System.out.println("setBoundingBox: "+box.toString());
		myBound=box;
		search(myWhat);
	}

	public void showDetailView(ClientGeoObject geoObject) {
		System.out.println("showDetailView");
		
			final DetailView waitingDeVi = geoMap.createDetailView(geoObject);
		///TODO benotigt ObjectDetail		
			//detailView.setDetail(....);
			final String wasSollISagen="testWaitsuper";
			
			Timer t = new Timer(){
				public void run() {
					waitingDeVi.setDetail(wasSollISagen+": "+Math.random());					
				}				
			};
			t.schedule(1000);
	}
	
	public GeoMap getGeoMap(){
		return geoMap;
	}

}
