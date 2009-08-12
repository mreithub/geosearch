package at.fakeroot.sepm.client;

import com.google.gwt.core.client.EntryPoint;

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
		GeoManager geoManager = new GeoManager();
		geoManager.drawGUI();
		
	}
}