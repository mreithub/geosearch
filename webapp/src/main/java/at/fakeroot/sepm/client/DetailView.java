package at.fakeroot.sepm.client;

import at.fakeroot.sepm.shared.client.serialize.ClientGeoObject;
import at.fakeroot.sepm.shared.client.serialize.ObjectDetails;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Class containing the object details shown in the InfoWindow
 * @author AC
 */

public class DetailView extends InfoWindowContent implements ClickHandler{
	
	private IGeoManager gManager =null;
	private ClientGeoObject gObject=null;
	private static ScrollPanel scroll = new ScrollPanel();
	private VerticalPanel myVePa=new VerticalPanel();
	private HTML title = null;
	private HTML detail = null;
	private FlowPanel tagPanel= null;
	private HorizontalPanel myHoPa = new HorizontalPanel();
	private Anchor sourceAnchor;
	private HTML image;
	
	/**
	 * Constructor. Builds the look of the DetailView
	 * @param object ClientGeoObject the object for which the DetailView is built
	 * @param geoManager IGeoManager is needed to communicate with the GeoManager
	 */
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
		tagPanel = new FlowPanel();
		setTagList(gObject.getTags());
		
		myVePa.setWidth("325px");
		myVePa.add(title);
		myVePa.add(new HTML("<br/>"));
		myVePa.add(detail);
		myVePa.add(myHoPa);
		myVePa.add(new HTML("<hr color=\"#FF3399\" size=\"1\">"));
		myVePa.add(new HTML("<b><font size=\"3\"><span style=\"color:#3366FF\">Tags:</span></font></b>"));
		myVePa.add(tagPanel);

	}
	
	/**
	 * sets the object details in the InfoWindow
	 * @param HTMLStr String text that will be interpreted as HTML
	 * @param link String the link to the source of the picture, article, event,...
	 * @param thumbnail String the link to the thumbnail of the service-logo, which is displayed
	 * @param homepage String the link to the homepage of the given service
	 */
	public void setDetail(String HTMLStr, String link, String thumbnail, String homepage){
		detail.setHTML(HTMLStr + "<br/><hr color=\"#FF3399\" size=\"1\">");
		sourceAnchor.setText("Details...");
		//the thumbnail is a link to the homepage
		image.setHTML("<a href=\"" + homepage + "\"><img src=\"" + thumbnail + "\" border=\"0\">");
		image.setVisible(true);
		
		sourceAnchor.setHref(link);
		sourceAnchor.setVisible(true);
		
		int h = myVePa.getOffsetHeight();
		h = Math.min(300, h+20);
		scroll.setSize("350px", h+"px");
	}
	
	public void setDetail(ObjectDetails details) {
		setDetail(details.getHTMLString(), details.getLink(), details.getThumbnail(), details.getHomepage());
		setTagList(details.getTags());
	}
	
	/**
	 * private method used to set the tag list in the InfoWindow
	 * */	
	private void setTagList(String tags[]) {
		final Anchor[] anchor = new Anchor[tags.length];
		for(int i=0; i<tags.length; i++){
			anchor[i]=new Anchor("<font size=\"2\">" + tags[i] + "</font>", true);
			anchor[i].setHref("javascript:void()");
			anchor[i].addClickHandler(this);
			tagPanel.add(anchor[i]);
			tagPanel.add(new InlineLabel(" "));
		}
	}

	/**
	 * inherited method from ClickHandler-interface
	 * */
	public void onClick(ClickEvent ce) {
		gManager.addSearchTag(((Anchor)ce.getSource()).getText());
	}

	/**
	 * A static method to set the size of the ScrollPanel in the Constructor's super() call
	 * @return ScrollPanel with the desired size
	 */
	private static ScrollPanel setSize()
	{
		scroll.setSize("350px", "150px");
		return scroll;
	}
	
}
