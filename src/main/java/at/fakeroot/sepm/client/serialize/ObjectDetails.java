package at.fakeroot.sepm.client.serialize;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ObjectDetails implements IsSerializable
{
	private String HTMLString;
	private String[] tags;
	
	/**
	 * StandardConstruction. Is required for the serializeation
	 */
	public ObjectDetails()
	{
	}
	
	/**
	 * Constructor.
	 * @param HTMLString The detail HTML String.
	 * @param tags The tag array.
	 */
	public ObjectDetails(String HTMLString, String[] tags)
	{
		this.HTMLString = HTMLString;
		this.tags = tags;
	}
	
	/**
	 * @return The HTML detail string.
	 */
	public String getHTMLString()
	{
		return (HTMLString);
	}
	
	/**
	 * @return The tag String array.
	 */
	public String[] getTags()
	{
		return(tags);
	}
}
