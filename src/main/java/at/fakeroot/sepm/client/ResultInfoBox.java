package at.fakeroot.sepm.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * The InfoBox informs the User about the number of results for the recent search
 * @author RK
 */
public class ResultInfoBox extends Composite
{
	Label numText;
	
	public ResultInfoBox()
	{
		VerticalPanel vPanel = new VerticalPanel();
		numText = new Label();
		numText.setSize("200px", "30px");
		Label zoomText = new Label("Fuer Details bitte hineinzoomen!");
		zoomText.setSize("200px", "30px");
		vPanel.add(numText);
		vPanel.add(zoomText);
		initWidget(vPanel);
	}
	
	/**
	 * sets the text, which is displayed in the InfoBox, depending on the number of 
	 * results of the recent search
	 * @param shown the number of results, which are shown on the map
	 * @param available the number of results, which are found in the area
	 */
	public void refresh(int shown, int available)
	{
		if(shown == 0)
			numText.setText("Ihre Suche ergab keinen Treffer.");
		else if(shown == 1)
			numText.setText("" + shown + " Ergebnis von " + available);
		else
			numText.setText(shown + " Ergebnisse von " + available);
	}
}
