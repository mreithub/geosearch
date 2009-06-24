package at.fakeroot.sepm.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GeoSearch implements EntryPoint
{
	/**
	* This is the entry point method.
	*/
	public void onModuleLoad()
	{
		GeoManager myManager = new GeoManager();
		RootPanel.get().add(myManager.getGeoMap());
		myManager.drawGUI();
		
	}
}