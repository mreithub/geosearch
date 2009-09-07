package at.fakeroot.sepm.client;

import at.fakeroot.sepm.shared.client.serialize.BoundingBox;
import at.fakeroot.sepm.shared.client.serialize.ClientGeoObject;

/**
 * The interface for the GeoManager - specifies all functions which have to be implemented there.
 * @author MK
 */
public interface IGeoManager
{
	public void addSearchTag(String tag);
	public void searchByLocationAndTags(String location);
	public void searchByTags(String tags);
	public void setBoundingBox(BoundingBox box);
	public void showDetailView(ClientGeoObject obj);
	public void clearWhereString();
	
	public void showErrorMessage(String msg, String detail);
	public void showServerError(String msg);
	public void showRegionError();
}
