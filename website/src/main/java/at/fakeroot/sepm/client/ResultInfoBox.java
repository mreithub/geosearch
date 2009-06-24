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
	Label zoomText;
	
	public ResultInfoBox()
	{
		VerticalPanel vPanel = new VerticalPanel();
		numText = new Label();
		numText.setSize("240px", "30px");
		zoomText = new Label("Zoom in for further results.");
		zoomText.setSize("240px", "30px");
		zoomText.setVisible(false);
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
		{
			numText.setText("No results found.");
			zoomText.setVisible(false);
		}
		else if(shown == 1)
		{
			numText.setText(shown + " result");
			zoomText.setVisible(false);
		}
		else
		{
			if(available > -1 && shown == available)
			{
				numText.setText(shown + " results");
				zoomText.setVisible(false);
			}
			else
			{
				numText.setText(shown + " results out of more than " + available);
				zoomText.setVisible(true);
			}
		}
	}
}
