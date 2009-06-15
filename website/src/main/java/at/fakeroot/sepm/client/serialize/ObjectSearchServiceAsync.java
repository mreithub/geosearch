package at.fakeroot.sepm.client.serialize;

import com.google.gwt.user.client.rpc.AsyncCallback;
import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import at.fakeroot.sepm.shared.client.serialize.ObjectDetails;
import at.fakeroot.sepm.shared.client.serialize.SearchResult;

public interface ObjectSearchServiceAsync 
{
	public void search(BoundingBox box, String what, AsyncCallback<SearchResult> callBack);
	
	public void getDetailHTML(long objId, AsyncCallback<ObjectDetails> callBack);
}


