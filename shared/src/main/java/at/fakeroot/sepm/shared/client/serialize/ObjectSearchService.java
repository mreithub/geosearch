package at.fakeroot.sepm.shared.client.serialize;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import at.fakeroot.sepm.shared.client.serialize.SearchResult;

@RemoteServiceRelativePath("objectsearch")
public interface ObjectSearchService extends RemoteService
{
	public SearchResult search(BoundingBox box, String what);
	
	public ObjectDetails getDetailHTML(int objId);
}

