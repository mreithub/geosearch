package at.fakeroot.sepm.geoSearch.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("objectsearch")
public interface ObjectSearchService extends RemoteService
{
	public SearchResult search(BoundingBox box, String what);
	
	public ObjectDetails getDetailHTML(int objId);
}

