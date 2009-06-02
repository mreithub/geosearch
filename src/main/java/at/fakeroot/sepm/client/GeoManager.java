package at.fakeroot.sepm.client;

import at.fakeroot.sepm.client.serialize.BoundingBox;
import at.fakeroot.sepm.client.serialize.ClientGeoObject;
import at.fakeroot.sepm.client.serialize.ObjectDetails;
import at.fakeroot.sepm.client.serialize.ObjectSearchService;
import at.fakeroot.sepm.client.serialize.ObjectSearchServiceAsync;
import at.fakeroot.sepm.client.serialize.SearchResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Manage all requests between the Objects and the server.
 * @author JB
 *
 */
public class GeoManager implements IGeoManager {
	
	private final ObjectSearchServiceAsync objectSearch = GWT.create(ObjectSearchService.class);
	private int XOFFSET=5;
	private int YOFFSET=5;
	private Image logo = new Image("images/design/logo_no_shaddow.png");
	private SearchBox searchBox;
	private ResultInfoBox resultBox;
	private TagCloud tagCloud;
	private GeoMap geoMap;
	
	private String myWhat="";
	private BoundingBox myBound;
	
	public GeoManager() {
		this.geoMap=new GeoMap(this);
		this.searchBox=new SearchBox(this);
		this.tagCloud = new TagCloud(this);
		this.resultBox = new ResultInfoBox();		
	}
	
	/**
	 * Draws the GUI after creating the object. (Has to be run after inserted to container)
	 */
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
	
	/**
	 * Adds a SearchTag to the current search, and searchbox
	 * @param tag 
	 */
	public void addSearchTag(String tag) {
		this.searchBox.setWhat(searchBox.getWhat()+" "+tag);
		
	}

	/**
	 * Starts a standard search
	 * @param where eg.: Linz, Wien
	 * @param what eg.: kirche
	 */
	public void search(String where, String what) {
		myWhat=what;
		geoMap.search(where);
		search(myWhat);
	}

	/**
	 * Starts a search with available BoundingBox 
	 * @param what eg.: kirche
	 */
	public void search(String what) {
		objectSearch.search(myBound, what.trim(), new AsyncCallback<SearchResult>()
				{
					public void onFailure(Throwable arg0) {
						System.err.println( "" + arg0.getMessage() +"  error");	
					}

					public void onSuccess(SearchResult result) {
						geoMap.setPins(result.getResults());
						tagCloud.refresh(result.getResults().iterator());
						resultBox.refresh(result.getResults().size(), result.getResultCount());
					}
					
				});
		
	}

	/**
	 * Sets the rectangle where the search engine will search.
	 * @param box  
	 */
	public void setBoundingBox(BoundingBox box) {
		myBound=box;
		search(myWhat);
	}

	/**
	 * Open the DetailView of an ClientGeoObject
	 * @param geoObject 
	 */
	public void showDetailView(ClientGeoObject geoObject) {
		System.out.println("showDetailView");
		
		final DetailView waitingDeVi = geoMap.createDetailView(geoObject);
		
		objectSearch.getDetailHTML(10, new AsyncCallback<ObjectDetails>(){
			public void onFailure(Throwable arg0) {
				System.err.println( "" + arg0.getMessage() +"  error");	
			}

			public void onSuccess(ObjectDetails result) {
				waitingDeVi.setDetail(result.getHTMLString());				
			}
			
		});
		
		
		///TODO benotigt ObjectDetail		
		//detailView.setDetail(....);
		/*
		final String wasSollISagen="testWaitsuper";
		
		
		Timer t = new Timer(){
			public void run() {
				waitingDeVi.setDetail(wasSollISagen+": "+Math.random());					
			}				
		};
		t.schedule(1000);
		*/
	}
	
	/**
	 * Returns a MapWidget
	 * @return
	 */
	public GeoMap getGeoMap(){
		return geoMap;
	}

}
