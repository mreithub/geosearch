package at.fakeroot.sepm.client;

import at.fakeroot.sepm.shared.client.serialize.SearchResult;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
/**
 * The InfoBox informs the User about the number of results for the recent search
 * @author RK
 */
public class ResultInfoBox extends Composite
{
	Label title;
	Label numText;
	Label zoomText;
	InlineLabel durationText; 

	private class LoadingTimer extends Timer {
		@Override
		public void run() {
			String newText = numText.getText();
			newText += ".";
			if (newText.equals("Loading....")) newText = "Loading";
			numText.setText(newText);
		}
	}
	
	Timer loadingTimer = new LoadingTimer();

	/**
	 * Constructor. Sets the Layout of the Box
	 */
	public ResultInfoBox()
	{
		FlowPanel fPanel = new FlowPanel();
		fPanel.getElement().setId("resultInfoBox");
		
		durationText = new InlineLabel();
		durationText.setStyleName("floatRight");
		
		title = new Label();
		title.setText("Result info");
		title.setStyleName("title");
		
		numText = new Label();
		zoomText = new Label("Zoom in for further results.");
		zoomText.setVisible(false);
		fPanel.add(durationText);
		fPanel.add(title);
		fPanel.add(numText);
		fPanel.add(zoomText);
		initWidget(fPanel);
	}
	
	/**
	 * sets the text, which is displayed in the InfoBox, depending on the number of 
	 * results of the recent search
	 * @param shown int the number of results, which are shown on the map
	 * @param available int the number of results, which are found in the area
	 * @param countLimit int the max limit for the Box; if there are more results in the DB (int available)
	 * @param duration time in milliseconds the search took
	 * then the countLimit there will be displayed 'int shown results out of more then int countLimit'  
	 */
	public void refresh(SearchResult result)
	{
		int shown = result.getResults().size();
		int available = result.getResultCount();
		
		// stop the timer
		stopLoading();
		
		//No results
		if(shown == 0)
		{
			numText.setText("No results found.");
		}
		//1 result
		else if(shown == 1)
		{
			numText.setText(shown + " result");
		}
		else
		{
			//there are as many results in the DB, as results are shown on the Map
			if(available > -1 && shown == available)
			{
				numText.setText(shown + " results");
			}
			else
			{
				//there are countLimit results (available is limited with countLimit)
				//which means exactly countLimit results or more then countLimit results
				if(available > -1 && available == result.getCountLimit())
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
		
		// print the duration
		durationText.setText((result.getDuration()/1000.)+" sec");
	}
	
	public void startLoading() {
		zoomText.setVisible(false);
		durationText.setVisible(false);
		numText.setText("Loading");
		loadingTimer.scheduleRepeating(500);
	}
	
	public void stopLoading() {
		loadingTimer.cancel();
		numText.setText("");
		durationText.setText("");
		durationText.setVisible(true);
	}
}
