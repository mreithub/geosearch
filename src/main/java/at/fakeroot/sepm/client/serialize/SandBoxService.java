package at.fakeroot.sepm.client.serialize;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("sandboxarray")
public interface SandBoxService extends RemoteService {
	ClientGeoObject[] getObjects(int id);
}
