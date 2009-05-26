package at.fakeroot.sepm.client.serialize;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("objectsearch")
public interface ObjectSearchService extends RemoteService
{
	public SearchResult search(BoundingBox box, String what);
}

