package at.fakeroot.sepm.server;

import at.fakeroot.sepm.client.serialize.ClientGeoObject;
import at.fakeroot.sepm.client.serialize.SandBoxService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class SandBoxServiceImpl extends RemoteServiceServlet implements SandBoxService {

	public ClientGeoObject[] getObjects(int id) {
		int count=(int)(Math.random()*20);
		ClientGeoObject[] myObj = new ClientGeoObject[count];
		for(int i=0;i<count;i++){
			ClientGeoObject tempGeo = new ClientGeoObject();
			tempGeo.setId(i);
			tempGeo.setTitel("titel: "+i);
			tempGeo.setXPos(Math.random());
			tempGeo.setYPos(Math.random());
			tempGeo.setImageUrl("imagUrl");
			String[] tags={"Tag1","Tag2"};
			tempGeo.setTags(tags);
			myObj[i]=tempGeo;
		}
		
		return myObj;
	}

}
