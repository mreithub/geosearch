package at.fakeroot.sepm.client.serialize;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import at.fakeroot.sepm.shared.client.serialize.ObjectDetails;
import at.fakeroot.sepm.shared.client.serialize.SearchResult;

@RemoteServiceRelativePath("objectsearch")
public interface ObjectSearchService extends RemoteService
{
	public SearchResult search(BoundingBox box, String what);
	
	public ObjectDetails getDetailHTML(int objId);
}

