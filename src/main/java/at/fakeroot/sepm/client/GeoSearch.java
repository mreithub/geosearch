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
		//RootPanel.get().add(new SandBox());
		//GeoMap map = new GeoMap();
		//RootPanel.get().add(map);
		GeoManager myManager = new GeoManager();
		RootPanel.get().add(myManager.getGeoMap());
		
	}
}