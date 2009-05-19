package at.fakeroot.sepm.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
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
		//RootPanel.get().add(new HTML("test"));
		//RootPanel.get().add(new SandBox());
		//RootPanel.get().add(new SearchBox(new GeoManager()));
		//GeoMap map = new GeoMap();
		//RootPanel.get().add(map);
		GeoManager myManager = new GeoManager();
		RootPanel.get().add(myManager.getGeoMap());
		myManager.drawGUI();
		//PopupPanel searchPop = new PopupPanel(true);
		//searchPop.setWidget(new Button("test"));
		//searchPop.show();
	}
}