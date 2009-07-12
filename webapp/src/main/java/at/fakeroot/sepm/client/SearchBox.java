package at.fakeroot.sepm.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;


/**
 * Class representing the Search Input Box.
 * @author AC
 */
public class SearchBox extends Composite{

	private HorizontalPanel hPanel= new HorizontalPanel();
	private FlowPanel fPanel = new FlowPanel();
	private IGeoManager geoManager=null;
	private TextBox where=new TextBox();
	private TextBox what=new TextBox();
	private Button searchButton = new Button("Search");

	private final String whereString = "";
	private final String whatString = "";

	/**
	 * Constructor
	 * @param gm a IGeoManager instance
	 * */
	public SearchBox(IGeoManager gm){
		hPanel.getElement().setId("searchBox");
		initWidget(hPanel);
		geoManager=gm;		

		where.setText(whereString);
		where.setTitle("Where to point the map at");
		where.setStyleName("emptyWhere");
		where.addFocusHandler(new FocusHandler(){
			public void onFocus(FocusEvent fe) {
				where.setStyleName("");	
			}
		});
		where.addBlurHandler(new BlurHandler(){
			public void onBlur(BlurEvent be) {
				if(where.getText().trim().length() == 0){
					where.setStyleName("emptyWhere");
				}
			}
		});
		where.addKeyPressHandler(new KeyPressHandler(){
			public void onKeyPress(KeyPressEvent kpe) {
				if(kpe.getCharCode() == KeyCodes.KEY_ENTER){
					performSearch();
				}
			}
		});		


		what.setText(whatString);
		what.setTitle("What to search for");
		what.setStyleName("emptyWhat");
		what.addFocusHandler(new FocusHandler(){
			public void onFocus(FocusEvent fe) {
				what.setStyleName("");
			}
		});
		what.addBlurHandler(new BlurHandler(){
			public void onBlur(BlurEvent be) {
				if(what.getText().trim().length() == 0){
					what.setStyleName("emptyWhat");
				}
			}
		});		
		what.addKeyPressHandler(new KeyPressHandler(){
			public void onKeyPress(KeyPressEvent kpe) {
				if(kpe.getCharCode() == KeyCodes.KEY_ENTER)
					performSearch();
			}
		});

		searchButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent ce) {
				performSearch();
			}
		});


		where.setWidth("160px");
		what.setWidth("160px");
		searchButton.setSize("80px","55px");
		fPanel.setHeight("55px");
		fPanel.add(where);
		fPanel.add(what);
		hPanel.add(fPanel);
		hPanel.setCellHeight(fPanel, "55px");
		hPanel.add(searchButton);
	}

	/**
	 * Performs the actual search.
	 */
	private void performSearch()
	{
		geoManager.searchByLocationAndTags(getWhere());
	}

	/**
	 * Returns String currently in the where-TextBox
	 * @return retString where-search String
	 * */	
	public String getWhere(){
		String retString = where.getText().trim();
		if (retString.toLowerCase().equals(whereString.toLowerCase()))
			return ("");
		else
			return (retString);
	}

	/**
	 * Returns String currently in the what-TextBox
	 * @return retString String what-search String 
	 * */
	public String getWhat(){
		String retString = what.getText().trim();
		if (retString.toLowerCase().equals(whatString.toLowerCase()))
			return ("");
		else
			return (retString);
	}


	/**
	 * Sets String in the where-TextBox for the area search
	 * @param location 
	 * */
	public void setWhere(String location) {
		if (location.trim().length() == 0)
			where.setText(whereString);
		else
			where.setText(location);
	}


	/**
	 * Adds String to the what-TextBox for the tag search
	 * @param tagString 
	 * */	
	public void setWhat(String tagString){
		if (tagString.trim().length() == 0)
			what.setText(whatString);
		else
			what.setText(tagString);
	}
}