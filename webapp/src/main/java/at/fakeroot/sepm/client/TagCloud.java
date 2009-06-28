package at.fakeroot.sepm.client;

import at.fakeroot.sepm.shared.client.serialize.ClientGeoObject;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * TagCloud-Widget class
 * 
 * Draws a TagCloud of the tags within the ClientGeoObjects given to refresh()
 * @author Manuel Reithuber
 */
public class TagCloud extends Composite implements ClickHandler {
	/**
	 * Comparator class that compares the tag statistic elements by their reference count.
	 * Used to sort the tag list by reference count to remove the least frequent ones if necessary.
	 *   
	 * @author Manuel Reithuber
	 */
	private class ValueComparator implements Comparator<String> {
		Map<String, Integer> m_map;
		
		/**
		 * Constructor
		 * @param map tagStat Map
		 */
		private ValueComparator(Map<String, Integer> map) {
			m_map = map;
		}
		
		@Override
		public int compare(String key1, String key2) {
			int v1 = m_map.get(key1);
			int v2 = m_map.get(key2);

			int rc = 1;
			if (v1 < v2) rc = -1;
			else if (v1 == v2) {
				int h1 = key1.hashCode();
				int h2 = key2.hashCode();
				
				if (h1 < h2) rc = -1;
				else if (h1 == h2) rc = 0;
			}
			return rc;
		}
	}

	/**
	 * Panel in which the tags are drawn
	 */
	private FlowPanel tagPanel = new FlowPanel();
	
	/**
	 * Widget Container
	 */
	private VerticalPanel vPanel = new VerticalPanel();
	
	/**
	 * Tag reference count statistics
	 */
	private HashMap<String, Integer> tagStat = new HashMap<String, Integer>();
	
	/**
	 * GeoManager reference
	 */
	private IGeoManager geoManager;
	
	private int objectCount = 0; 
	
	/**
	 * Constructor
	 * @param gm GeoManager reference
	 */
	public TagCloud(IGeoManager gm) {
		geoManager = gm;
		init();
	}
	
	/**
	 * Widget initialization
	 */
	private void init() {
		Label l = new Label("TagCloud");
		l.setHeight("1.2em");
		vPanel.add(l);
		vPanel.setCellHeight(l, "1.2em");
		initWidget(vPanel);
		setWidth("240px");
		
		Style s = l.getElement().getStyle();
		s.setProperty("borderBottom", "1px solid black");
		vPanel.add(tagPanel);
	}
	
	/**
	 * Add a String array of tags to the tag count statistics (tagStat)
	 * 
	 * Internal function, called by refresh()
	 * @param tags tags to be added
	 */
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
	
	/**
	 * If there are more than 20 tags in the TagCloud, remove those with the least reference count.
	 * Additionally, remove all tags that all GeoObjects have
	 */
	private void cleanupTags() {
		if (tagStat.size() > 20) {
			ValueComparator comp = new ValueComparator(tagStat);
			TreeMap<String, Integer> tmp = new TreeMap<String, Integer>(comp);
			
			tmp.putAll(tagStat);

			Set<String> keys = tmp.keySet();
			Iterator<String> it = keys.iterator();

			int i = 0;
			while (it.hasNext()) {
				String k = it.next();
				if (k != null && tagStat.size() > 20) {
					tagStat.remove(k);
				}
				else if (tagStat.get(k) == objectCount) {
					tagStat.remove(k);
				}
				i++;
			}
		}
	}
	
	/**
	 * creates Anchor widgets for the tags, resizes them based on their reference count and adds ClickHandlers to them
	 * 
	 * Internal function, called by refresh()
	 */
	private void drawTags() {
		Style s = null;
		Anchor a;
		String tag;
		int i;
		
		cleanupTags();
		
		Set<String> c = tagStat.keySet();
		
		int min = tagStat.size(), max = 0;

		// get the minimal and maximal Tag frequency
		Iterator<String> it = c.iterator();
		while (it.hasNext()) {
			tag = it.next();
			
			i = tagStat.get(tag);
			if (i < min) min = i;
			if (i > max && i < objectCount) max = i;
		}
		
		it = c.iterator();
		
		int fontsize = 100;
		while (it.hasNext()) {
			tag = it.next();
			a = new Anchor(tag);
			a.setTitle(tag);
			a.setHref("javascript:void()");

			Element e = a.getElement();
			if (e != null) s = e.getStyle();
			else s = null;
			// calculation source: German Wikipedia
			if (max > min) fontsize = (int) (70+Math.ceil(120*(tagStat.get(tag)-min))/(max-min));

			if (s != null) s.setProperty("fontSize", fontsize+"%");

			a.addClickHandler(this);
			tagPanel.add(a);
			tagPanel.add(new InlineLabel(" ")); 
		}
	}
	
	/**
	 * Refresh the TagCloud based on the tags given by @c it
	 * @param it ClientGeoObject iterator that points at the new list of ClientGeoObjects
	 */
	public void refresh(Iterator<ClientGeoObject> it) {
		ClientGeoObject o;

		tagStat.clear();
		objectCount = 0;

		while (it.hasNext()) {
			o = it.next();
			addTags(o.getTags());
			objectCount++;
		}

		tagPanel.clear();

		if (tagStat.size() > 0) {
			drawTags();
		}
		else {
			Label l = new Label("No tags found!");
			Element e = l.getElement();
			e.setAttribute("fontVariant", "italic");
			e.setAttribute("color", "#ccc");
			tagPanel.add(l);
		}
	}

	/**
	 * ClickEventHandler for the tag Anchors
	 */
	@Override
	public void onClick(ClickEvent e) {
		// Tag clicked
		if (e.getSource() instanceof Anchor) {
			Anchor a = (Anchor) e.getSource();
			geoManager.addSearchTag(a.getText());
		}
	}
}
