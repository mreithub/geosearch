package at.fakeroot.sepm.client.serialize;

import com.google.gwt.user.client.rpc.AsyncCallback;
import at.fakeroot.sepm.geoSearch.shared.BoundingBox;
import at.fakeroot.sepm.geoSearch.shared.ObjectDetails;
import at.fakeroot.sepm.geoSearch.shared.SearchResult;

public interface ObjectSearchServiceAsync 
{
	public void search(BoundingBox box, String what, AsyncCallback<SearchResult> callBack);
	
	public void getDetailHTML(int objId, AsyncCallback<ObjectDetails> callBack);
}


