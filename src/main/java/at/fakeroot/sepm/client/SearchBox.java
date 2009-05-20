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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Class representing the Search Input Box.
 * @author Anca Cismasiu
 */
public class SearchBox extends Composite{

	private HorizontalPanel myHoPanel= new HorizontalPanel();
	private VerticalPanel myVePanel = new VerticalPanel();
	private IGeoManager geoManager=null;
	private TextBox where=new TextBox();
	private TextBox what=new TextBox();
	private Button searchButton = new Button("Search"); 

	public SearchBox(IGeoManager gm){
		initWidget(myHoPanel);
		geoManager=gm;
		where.addKeyPressHandler(new KeyPressHandler(){
			public void onKeyPress(KeyPressEvent kpe) {
				if(kpe.getCharCode()==KeyCodes.KEY_ENTER){
					geoManager.search(where.getText(), what.getText());
				}
			}
		});
		
		where.setText("Where?");
		
		where.addFocusHandler(new FocusHandler(){
			public void onFocus(FocusEvent fe) {
				if(where.getText().equals("Where?"))
					clearBox(where);			
			}
		});
		
		where.addBlurHandler(new BlurHandler(){
			public void onBlur(BlurEvent be) {
				if(where.getText().equals("")){
					where.setText("Where?");
				}
			}
			
		});
		
		
		what.addKeyPressHandler(new KeyPressHandler(){
			public void onKeyPress(KeyPressEvent kpe) {
				if(kpe.getCharCode()==KeyCodes.KEY_ENTER)
				geoManager.search(where.getText(), what.getText());
			}
		});
		
		what.setText("What?");
		
		what.addFocusHandler(new FocusHandler(){
			public void onFocus(FocusEvent fe) {
				if(what.getText().equals("What?"))
					clearBox(what);				
			}
		});
		
		what.addBlurHandler(new BlurHandler(){
			public void onBlur(BlurEvent be) {
				if(what.getText().equals("")){
					what.setText("What?");
				}
			}
			
		});
		
		searchButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent ce) {					
				geoManager.search(getWhere(), getWhere());
		      }
		});
		
		searchButton.setSize("80px","45px");
		myVePanel.add(where);
		myVePanel.add(what);
		myHoPanel.add(myVePanel);
		myHoPanel.add(searchButton);
		
	}

	/**
	 * Clears TextBox
	 * @param box TextBox to be cleared 
	 * */
	public void clearBox(TextBox box){
		box.setText("");
	}
	
	/**
	 * Returns String currently in the where-TextBox
	 * @return rc String where search String
	 * */	
	public String getWhere(){
		String rc="";
		if(!where.getText().equals("Where?"))
			rc=where.getText();
		 return rc;
	}
	
	/**
	 * Returns String currently in the what-TextBox
	 * @return rc String what search String 
	 * */
	public String getWhat(){
		String rc="";
		if(!what.getText().equals("What?"))
			rc=what.getText();
		return rc;
	}
	
	
	/**
	 * Sets String in the where-TextBox for the area search
	 * @param whereStr 
	 * */
	public void setWhere(String whereStr) {
		where.setText(whereStr);
	}
	
	
	/**
	 * Adds String to the what-TextBox for the tag search
	 * @param whatStr 
	 * */	
	public void setWhat(String whereStr){
		String s = what.getText();
		what.setText(s+" "+whereStr);
		geoManager.search(getWhere(), getWhat());
	}

	
}