package at.fakeroot.sepm.server;

import java.util.ArrayList;

import at.fakeroot.sepm.client.serialize.BoundingBox;
import at.fakeroot.sepm.client.serialize.ClientGeoObject;
import at.fakeroot.sepm.client.serialize.ObjectSearchService;
import at.fakeroot.sepm.client.serialize.SearchResult;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ObjectSearchServiceImpl extends RemoteServiceServlet implements ObjectSearchService
{

	private static final int limit = 50;
	// private GeoObjectManager geoObjManager; (nach MR2)
	private ArrayList<ClientGeoObject> fakeDB;
	
	public ObjectSearchServiceImpl()
	{
		//geoObjManager = GeoObjectManager.getInstance(); (nach MR2)
		fakeDB = new ArrayList<ClientGeoObject>();
		fakeDB.add(new ClientGeoObject(1, "Karlskirche", "wiki.png", new String[] {"karlskirche","kirche", "karlsplatz", "sehenswürdigkeit"}, 48.198247, 16.371422));
		fakeDB.add(new ClientGeoObject(2, "Stephansdom", "wiki.png", new String[] {"stephansdom","kirche", "stephansplatz", "wahrzeichen", "wien", "sehenswürdigkeit", "mittelalter"}, 48.208333, 16.372778));
		fakeDB.add(new ClientGeoObject(3, "Schloss SchÃ¶nbrunn", "wiki.png", new String[] {"schloss","schÃ¶nbrunn", "schlosspark", "neptunbrunnen", "sehenswürdigkeit"}, 48.184517, 16.311864));				
		fakeDB.add(new ClientGeoObject(4, "Goldenes Dachl", "wiki.png", new String[] {"goldenes","dachl", "innsbruck", "wahrzeichen", "sehenswürdigkeit", "mittelalter"}, 47.268583, 11.393264));
		fakeDB.add(new ClientGeoObject(5, "Festung Hohensalzburg", "wiki.png", new String[] {"festung","hohensalzburg", "salzburg", "wahrzeichen", "burg", "sehenswürdigkeit", "mittelalter"}, 47.794967, 13.047256));
		fakeDB.add(new ClientGeoObject(6, "Wallfahrtsbasilika PÃ¶stlingberg", "wiki.png", new String[] {"wallfahrtsbasilika","pÃ¶stlingberg", "linz", "wahrzeichen", "kirche", "sehenswürdigkeit"}, 48.323889, 14.258333));
		fakeDB.add(new ClientGeoObject(7, "Uhrturm", "wiki.png", new String[] {"uhrturm", "turm", "graz", "wahrzeichen", "sehenswürdigkeit", "mittelalter"}, 47.075463, 15.436746));
		fakeDB.add(new ClientGeoObject(8, "Martinsturm", "wiki.png", new String[] {"martinsturm", "turm", "bregenz", "wahrzeichen", "sehenswürdigkeit"}, 47.505, 9.749167));
		fakeDB.add(new ClientGeoObject(9, "Schloss Esterhazy ", "wiki.png", new String[] {"schloss","esterhazy ", "eisenstadt", "wahrzeichen", "sehenswürdigkeit", "mittelalter"}, 47.848611, 16.520833));
		fakeDB.add(new ClientGeoObject(10, "Lindwurm", "wiki.png", new String[] {"lindwurm","klagenfurt", "wahrzeichen", "sehenswürdigkeit"}, 46.623997, 14.3077));
		fakeDB.add(new ClientGeoObject(11, "Rathaus", "wiki.png", new String[] {"rathaus","sankt", "st.", "pÃ¶lten", "wahrzeichen", "sehenswürdigkeit", "mittelalter"}, 48.2, 15.616667));
	}
	
	public SearchResult search(BoundingBox box, String what)
	{
		SearchResult result = new SearchResult();
		String[] tags = what.toLowerCase().split(" ");

		int counter = 0;
		
		for(ClientGeoObject cgo : fakeDB)
		{
			if(cgo.getXPos() >= box.getX1() && cgo.getYPos() >= box.getY1() && cgo.getXPos() <= box.getX2() && cgo.getYPos() <= box.getY2())
			{
				
				boolean matches = true;
				if (tags.length != 1 || !tags[0].equals("")) {
					for (int i = 0; i < tags.length; i++) {
						boolean tagMatch = false;
						String[] cgoTags = cgo.getTags();
						for (int j = 0; j < cgoTags.length; j++) {
							if (tags[i].equals(cgoTags[j])) {
								tagMatch = true;
								break;
							}
						}
						if (tagMatch == false) {
							matches = false;
							break;
						}
					}
				}
				if(matches == true)
				{
					result.addResultToList(cgo);
					counter++;
				}
			}
		}
		result.setResultCount(counter);
		return result;
		//return (geoObjManager.select(tags, box, limit)); (nach MR2)
		
	}
	
}
