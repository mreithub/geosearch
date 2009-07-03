package at.fakeroot.sepm.client;

import com.google.gwt.user.client.ui.Composite;
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
	
	/**
	 * Constructor. Sets the Layout of the Box
	 */
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
	 * @param shown int the number of results, which are shown on the map
	 * @param available int the number of results, which are found in the area
	 * @param countLimit int the max limit for the Box; if there are more results in the DB (int available)
	 * then the countLimit there will be displayed 'int shown results out of more then int countLimit'  
	 */
	public void refresh(int shown, int available, int countLimit)
	{
		//No results
		if(shown == 0)
		{
			numText.setText("No results found.");
			zoomText.setVisible(false);
		}
		//1 result
		else if(shown == 1)
		{
			numText.setText(shown + " result");
			zoomText.setVisible(false);
		}
		else
		{
			//there are as many results in the DB, as results are shown on the Map
			if(available > -1 && shown == available)
			{
				numText.setText(shown + " results");
				zoomText.setVisible(false);
			}
			else
			{
				//there are countLimit results (available is limited with countLimit)
				//which means exactly countLimit results or more then countLimit results
				if(available > -1 && available == countLimit)
				{
					numText.setText(shown + " results out of more than " + available);
					zoomText.setVisible(true);
				}
				else
				{
					//there are less then countLimit results
					numText.setText(shown + " results out of " + available);
					zoomText.setVisible(true);
				}
			}
		}
	}
}