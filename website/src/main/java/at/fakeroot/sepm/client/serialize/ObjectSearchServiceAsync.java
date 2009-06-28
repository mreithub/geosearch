package at.fakeroot.sepm.client.serialize;

import com.google.gwt.user.client.rpc.AsyncCallback;
import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import at.fakeroot.sepm.shared.client.serialize.ObjectDetails;
import at.fakeroot.sepm.shared.client.serialize.SearchResult;

/**
 * An Interface, which declares the same methods as the Interface ObjectSearchService, just
 * in an asynchronous way - which means the type of the return value that was declared in 
 * the synchronous version of the method is now the Type of the AsyncCallback <T>
 * @author RK
 */
public interface ObjectSearchServiceAsync 
{
	public void search(BoundingBox box, String what, AsyncCallback<SearchResult> callBack);
	
	public void getDetailHTML(long objId, AsyncCallback<ObjectDetails> callBack);
}


