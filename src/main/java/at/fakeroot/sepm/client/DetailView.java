package at.fakeroot.sepm.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import at.fakeroot.sepm.client.serialize.*;

public class DetailView {
	
	private IGeoManager gManager =null;
	private ClientGeoObject gObject=null;
	private VerticalPanel myVePa = null;
	private HTML title = null;
	private HTML detail = null;
	private HTML tags= null;
	
	
	public DetailView(ClientGeoObject obj, IGeoManager gm){
		
		gObject=obj;
		gManager=gm;
		title=new HTML(gObject.getTitel());
		detail = new HTML("Loading...");
		tags = new HTML(setTagList());
		myVePa=new VerticalPanel();
		myVePa.add(title);
		myVePa.add(detail);
		myVePa.add(tags);		
	}
	
	public void setDetail(String HTMLStr){
		detail.setHTML(HTMLStr);
	}
	
	
	private String setTagList(){
		String tagStr="";
		for(int i=0;i<gObject.getTags().length;i++){
			tagStr+=gObject.getTags()[i];
			tagStr+=" ";
		}
		return tagStr;
	}
}
