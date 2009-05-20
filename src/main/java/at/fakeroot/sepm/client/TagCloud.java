package at.fakeroot.sepm.client;

import at.fakeroot.sepm.client.serialize.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Text;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * TagCloud-UserInterface Class
 * 
 * Draws a TagCloud of the tags within the ClientGeoObjects
 * @author Manuel Reithuber
 */
public class TagCloud extends Composite implements ClickHandler {
	private FlowPanel tagPanel = new FlowPanel();
	private VerticalPanel vPanel = new VerticalPanel();
	private HashMap<String, Integer> tagStat = new HashMap<String, Integer>();
	private IGeoManager geoManager;
	
	public TagCloud(IGeoManager gm) {
		geoManager = gm;
		init();
	}
	
	private void init() {
		Label l = new Label("TagCloud");
		//l.setWidth("100%");
		l.setHeight("1.2em");
		vPanel.add(l);
		vPanel.setCellHeight(l, "1.2em");
		initWidget(vPanel);
		setWidth("200px");
		
		Style s = l.getElement().getStyle();
		s.setProperty("borderBottom", "1px solid black");
		vPanel.add(tagPanel);
		
		//tagPanel.setWidth("100%");

		// test data
		String[] t = {"wiki", "wiki", "wiki", "haus", "foo", "bla", "wiki", "bla"};
		addTags(t);
		drawTags();
	}
	
	private void addTags(String[] tags) {
		String tag;
		int i;

		for (i = 0; i < tags.length; i++) {
			tag = tags[i];
			if (tagStat.containsKey(tag)) {
				tagStat.put(tag, tagStat.get(tag)+1);
			}
			else tagStat.put(tag, 1);
		}
	}
	
	void drawTags() {
		Style s;
		Anchor a;
		String tag;
		Set<String> c = tagStat.keySet();
		
		Iterator<String> sIt = c.iterator();
		while (sIt.hasNext()) {
			tag = sIt.next();
			a = new Anchor(tag);
		
			s = a.getElement().getStyle();
			s.setProperty("fontSize", (100+(25*tagStat.get(tag)))+"%");

			a.addClickHandler(this);
			tagPanel.add(a);
			tagPanel.add(new InlineLabel(" ")); 
		}
	}
	
	public void refresh(Iterator<ClientGeoObject> it) {
		ClientGeoObject o;

		tagStat.clear();

		while (it.hasNext()) {
			o = it.next();
			addTags(o.getTags());
		}

		tagPanel.clear();
		drawTags();
	}

	@Override
	public void onClick(ClickEvent e) {
		// Tag clicked
		if (e.getSource() instanceof Anchor) {
			Anchor a = (Anchor) e.getSource();
			geoManager.addSearchTag(a.getText());
		}
	}
}
