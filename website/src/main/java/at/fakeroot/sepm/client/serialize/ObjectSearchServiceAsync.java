package at.fakeroot.sepm.client.serialize;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ObjectSearchServiceAsync 
{
	public void search(BoundingBox box, String what, AsyncCallback<SearchResult> callBack);
	
	public void getDetailHTML(int objId, AsyncCallback<ObjectDetails> callBack);
}


