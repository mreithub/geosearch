package at.fakeroot.sepm.client.serialize;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SandBoxServiceAsync {
	void getObjects(int id, AsyncCallback<ClientGeoObject[]> callBack);
}
