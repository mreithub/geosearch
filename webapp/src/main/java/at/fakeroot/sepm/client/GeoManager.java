package at.fakeroot.sepm.client;

import at.fakeroot.sepm.client.serialize.ObjectSearchService;
import at.fakeroot.sepm.client.serialize.ObjectSearchServiceAsync;
import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import at.fakeroot.sepm.shared.client.serialize.ClientGeoObject;
import at.fakeroot.sepm.shared.client.serialize.ObjectDetails;
import at.fakeroot.sepm.shared.client.serialize.SearchResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Manage all requests between the Objects and the server.
 * @author JB
 *
 */
public class GeoManager implements IGeoManager {
	private final ObjectSearchServiceAsync objectSearch = GWT.create(ObjectSearchService.class);
	private int XOFFSET=5;
	private int YOFFSET=5;
	private Image logo = new Image("images/design/logo_no_shadow.png");
	private SearchBox searchBox;
	private ResultInfoBox resultBox;
	private TagCloud tagCloud;
	private GeoMap geoMap;
	
	//The currently displayed region of the map, stored within a bounding box.
	private BoundingBox curBoundingBox;
	 
	public GeoManager() {
		geoMap=new GeoMap(this);
		searchBox=new SearchBox(this);
		tagCloud = new TagCloud(this);
		resultBox = new ResultInfoBox();		
	}
	
	/**
	 * Draws the GUI after creating the object. (Has to be run after inserted to container)
	 */
	public void drawGUI(){
		//Logo
		SimplePanel logoPnl = new SimplePanel();
		logo.setPixelSize(190, 40);
		logoPnl.setWidget(this.logo);
		logoPnl.getElement().setId("logo");
		RootPanel.get().add(logoPnl);

		//SearchBox
		PopupPanel searchPop = new PopupPanel(false);
		searchPop.setWidget(this.searchBox);
		searchPop.setPopupPosition(XOFFSET, YOFFSET);
		searchPop.show();		
		
		// ResultInfoBox + TagCloud; 
		PopupPanel infoPop = new PopupPanel(false);
		FlowPanel fp = new FlowPanel();
		
		fp.add(resultBox);
		fp.add(tagCloud);
		
		infoPop.setWidget(fp);
		infoPop.setPopupPosition(XOFFSET, YOFFSET+70);
		infoPop.show();
				
		//Use a history listener in order to be able to use a search history and command-line arguments.
		
		//TODO: This listener causes duplicate search queries and is therefore commented out for now. 
		
		//HistroyListener
		/*History.addValueChangeHandler(new ValueChangeHandler<String>(){
			String oldToken="";
			public void onValueChange(ValueChangeEvent<String> event) {
				System.out.println("histroy: "+event.getValue());
				if(!oldToken.equals(event.getValue()))
				{
					String[] tokens=event.getValue().split("&");
					for(int i=0;i<tokens.length;i++){
						if(tokens[i].split("=")[0].equals("pos")){
							System.out.println("pos");
							//String[] pos=tokens[i].split("=")[1].split(";");
							//geoMap.setCenter(Double.parseDouble(pos[1]), Double.parseDouble(pos[0]));
						}else if(tokens[i].split("=")[0].equals("q")){
							String[] var = tokens[i].split("=");
							if (var.length > 1)
							{
								searchBox.setWhat(var[1]);
								searchByTags(searchBox.getWhat());
							}
						}
					}
					
					
				}
				oldToken=event.getValue();
			}
		});
		
		History.fireCurrentHistoryState();*/
	}
	
	/**
	 * Adds a SearchTag to the current search, and searchbox
	 * @param tag 
	 */
	public void addSearchTag(String tag) {
		if (searchBox.getWhat().length() == 0)
			searchBox.setWhat(tag);
		else
			searchBox.setWhat(searchBox.getWhat() + " " + tag);
		searchByTags(searchBox.getWhat());
	}

	/**
	 * Performs a search for given tags at a given location. This function only takes
	 * the location string as parameter because the tag string is read out from the search
	 * box separately later. This is nescessary because we have to wait on the event handler
	 * function within the GeoMap to finish before we can process a search query.
	 * @param location eg.: Linz, Wien
	 */
	public void searchByLocationAndTags(String location) {
		//We have to wait until the geoMap finds the position where to search.
		//The actual search is therefore called from within the geoMap.
		geoMap.search(location);
	}

	/**
	 * Performs a search for given tags within the currently displayed region on the map.
	 * @param tags eg.: kirche
	 */
	public void searchByTags(String tags) {
		History.newItem("q="+tags.trim());
		resultBox.startLoading();
		objectSearch.search(curBoundingBox, tags.trim(), new AsyncCallback<SearchResult>()
		{
			public void onFailure(Throwable e) {
				showErrorMessage(e.getMessage());
				resultBox.stopLoading();
			}

			public void onSuccess(SearchResult result) {
				geoMap.setPins(result.getResults());
				tagCloud.refresh(result.getResults().iterator());
				resultBox.refresh(result);
				if (result.hasError()) {
					showErrorMessage(result.getErrorMessage());
				}
			}
		});		
	}

	/**
	 * Sets the rectangle where the search engine will search.
	 * @param box  
	 */
	public void setBoundingBox(BoundingBox box, boolean regionFound) {
		curBoundingBox = box;
		//Read out the what string again - it might has changed in the meantime.
		searchByTags(searchBox.getWhat());
		//if the user enters an area which doesn't exist (e.g. 'hahahahah') an InfoBox
		//will be shown
		if(regionFound == false && searchBox.getWhere().trim().length() != 0)
			areaNotFoundMessage();
	}
	
	/**
	 * This function clears the where input field of the search box.
	 */
	public void clearWhereString()
	{
		searchBox.setWhere("");
	}

	/**
	 * Open the DetailView of an ClientGeoObject
	 * @param geoObject 
	 */
	public void showDetailView(ClientGeoObject geoObject) {
		System.out.println("showDetailView");
		
		final DetailView waitingDeVi = geoMap.createDetailView(geoObject);
		
		objectSearch.getDetailHTML(geoObject.getId(), new AsyncCallback<ObjectDetails>(){
			public void onFailure(Throwable arg0) {
				System.err.println("" + arg0.getMessage() + " error");	
			}

			public void onSuccess(ObjectDetails result) {
				waitingDeVi.setDetail(result);
			}
			
		});
	}
	
	/**
	 * Returns a MapWidget
	 * @return
	 */
	public GeoMap getGeoMap(){
		return geoMap;
	}

	/**
	 * An ErrorMessage which is displayed, if there are problems with the Server or the AsyncCallback
	 * @param msg String the error-message
	 */
	public void showErrorMessage(String msg) {
		final DialogBox errBox = new DialogBox(false, true);
		VerticalPanel vPanel = new VerticalPanel();
		errBox.setWidget(vPanel);
		errBox.setText("Server Error!");
		
		vPanel.add(new Label(msg));
		Button ok = new Button("Ok");
		ok.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				errBox.hide();
			}
		});
		vPanel.add(ok);
		errBox.center();
	}
	
	/**
	 * A DialogBox which appears, if the user enters an area which doesn't exist (e.g. 'hahahahah')
	 */
	public void areaNotFoundMessage() {
		final DialogBox errBox = new DialogBox(false, true);
		VerticalPanel vPanel = new VerticalPanel();
		errBox.setWidget(vPanel);
		errBox.setText("Region not found!");
		
		vPanel.add(new Label("The region you searched for couldn't be found!"));
		Button ok = new Button("Ok");
		ok.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				errBox.hide();
			}
		});
		vPanel.add(ok);
		errBox.center();
	}
	
}
