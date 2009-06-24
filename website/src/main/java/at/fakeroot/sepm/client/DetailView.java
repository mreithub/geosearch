package at.fakeroot.sepm.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import at.fakeroot.sepm.shared.client.serialize.ClientGeoObject;

/**
 * @author Anca Cismasiu
 * Class containung the object details shown in the InfoWindow
 */

public class DetailView extends InfoWindowContent implements ClickHandler{
	
	private IGeoManager gManager =null;
	private ClientGeoObject gObject=null;
	//private static SimplePanel mySiPa = new SimplePanel();
	private static ScrollPanel scroll = new ScrollPanel();
	private VerticalPanel myVePa=new VerticalPanel();
	private HTML title = null;
	private HTML detail = null;
	private FlowPanel tags= null;
	private HorizontalPanel myHoPa = new HorizontalPanel();
	private Anchor sourceAnchor;
	private HTML image;
	
	
	public DetailView(ClientGeoObject object, IGeoManager geoManager){
		super(setSize());
		scroll.setWidget(myVePa);
		gObject=object;
		gManager=geoManager;
		
		title=new HTML("<font size=\"4\"><span style=\"color:#3366FF\"><b>" + gObject.getTitle() + "</b></span></font>");
		detail = new HTML("Loading...");
		image = new HTML();
		image.setVisible(false);
		
		sourceAnchor = new Anchor();
		sourceAnchor.setVisible(false);
		myHoPa.add(image);
		myHoPa.add(sourceAnchor);
		myHoPa.setCellWidth(image, "80%");
		myHoPa.setCellWidth(sourceAnchor, "20%");
		tags = new FlowPanel();
		setTagList();
		
		myVePa.add(title);
		myVePa.add(new HTML("<br/>"));
		myVePa.add(detail);
		myVePa.add(myHoPa);
		myVePa.add(new HTML("<hr color=\"#FF3399\" size=\"1\">"));
		myVePa.add(new HTML("<b><font size=\"3\"><span style=\"color:#3366FF\">Tags:</span></font></b>"));
		myVePa.add(tags);

	}
	
/**
 * sets the object details in the InfoWindow
 * @param HTMLStr String text that will be interpreted as HTML
 * */	
	public void setDetail(String HTMLStr, String link, String thumbnail, String homepage){
		detail.setHTML(HTMLStr + "<br/><hr color=\"#FF3399\" size=\"1\">");
		sourceAnchor.setText("Details...");
		image.setHTML("<a href=\"" + homepage + "\"><img src=\"" + thumbnail + "\" border=\"0\">");
		image.setVisible(true);
		
		sourceAnchor.setHref(link);
		sourceAnchor.setVisible(true);
	}
	
/**
 * private method used to set the tag list in the InfoWindow
 * 
 * */	
	private void setTagList() {
		String[] tagArray = gObject.getTags(); 
		final Anchor[] anchor = new Anchor[tagArray.length];
		for(int i=0; i<tagArray.length; i++){
			anchor[i]=new Anchor("<font size=\"2\">" + tagArray[i] + "</font>", true);
			anchor[i].setHref("javascript:void()");
			anchor[i].addClickHandler(this);
			tags.add(anchor[i]);
			tags.add(new InlineLabel(" "));
		}
	}

/**
 * inherited method from ClickHandler-interface
 * */
	public void onClick(ClickEvent ce) {
		gManager.addSearchTag(((Anchor)ce.getSource()).getText());
	}

	private static ScrollPanel setSize()
	{
		scroll.setSize("350px", "300px");
		return scroll;
	}
	
}
