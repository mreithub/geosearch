package at.fakeroot.sepm.client.serialize;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import at.fakeroot.sepm.shared.client.serialize.ObjectDetails;
import at.fakeroot.sepm.shared.client.serialize.SearchResult;
/**
 * The Interface for the ObjectSearchServiceImpl; it is an RPC Interface, that's 
 * why it extends the GWT RemoteService 
 * @author RK
 */

@RemoteServiceRelativePath("objectsearch")
public interface ObjectSearchService extends RemoteService
{
	public SearchResult search(BoundingBox box, String what);
	
	public ObjectDetails getDetailHTML(long objId);
}

