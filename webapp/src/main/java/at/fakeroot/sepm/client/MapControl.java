package at.fakeroot.sepm.client;

import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.ControlAnchor;
import com.google.gwt.maps.client.control.ControlPosition;
import com.google.gwt.maps.client.control.Control.CustomControl;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides a simple to use implementation of com.google.gwt.maps.client.control.Control
 * @author Manuel Reithuber
 */
public class MapControl extends CustomControl {
	private Widget m_widget;
	private boolean m_selectable;
	
	public MapControl(Widget widget, ControlPosition pos, boolean printable, boolean selectable) {
		super(pos, printable, selectable);
		m_selectable = selectable;
		m_widget = widget;
	}
	
	public MapControl(Widget widget, int xOffset, int yOffset, boolean printable, boolean selectable) {
		this(widget, new ControlPosition(ControlAnchor.TOP_LEFT, xOffset, yOffset), printable, selectable);
	}
	
	@Override
	protected Widget initialize(MapWidget map) {
		return m_widget;
	}

	@Override
	public boolean isSelectable() {
		return m_selectable;
	}
}
