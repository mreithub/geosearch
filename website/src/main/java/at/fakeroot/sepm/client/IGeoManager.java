package at.fakeroot.sepm.client;

import at.fakeroot.sepm.geoSearch.shared.BoundingBox;
import at.fakeroot.sepm.geoSearch.shared.ClientGeoObject;

/**
 * The interface for the GeoManager - specifies all functions which have to be implemented there.
 * @author MK
 */
public interface IGeoManager
{
	public void addSearchTag(String tag);
	public void search(String where, String what);
	public void search(String what);
	public void setBoundingBox(BoundingBox box);
	public void showDetailView(ClientGeoObject obj);
}