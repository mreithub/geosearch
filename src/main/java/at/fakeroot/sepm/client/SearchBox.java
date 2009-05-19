package at.fakeroot.sepm.client;

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
 * @author AC
 *
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
					where.setText(" ");
				if(where.getText().equals(""))
					where.setText("Where?");				
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
					what.setText(" ");
				if(what.getText().equals(""))
					what.setText("What?");				
			}
		});
		
		searchButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent ce) {					
				geoManager.search(where.getText(), what.getText());
		      }
		});
		
		searchButton.setSize("80px","45px");
		myVePanel.add(where);
		myVePanel.add(what);
		myHoPanel.add(myVePanel);
		myHoPanel.add(searchButton);
		
	}

	public String getWhere(){
		return where.getText();
	}
	
	public String getWhat(){
		return what.getText();
	}
	
	public void setWhere(String whereStr) {
		where.setText(whereStr);
	}
	
	public void setWhat(String searchStr){
		what.setText(searchStr);
		geoManager.search(where.getText(), what.getText());
	}

	
}