package at.fakeroot.sepm.shared.client.serialize;

import java.io.Serializable;

public class ObjectDetails implements Serializable
{
	/// default serial version id
	private static final long serialVersionUID = 1L;
	private String HTMLString;
	private String[] tags;
	private String link;
	private String thumbnail;
	
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
	public ObjectDetails(String HTMLString, String[] tags, String link, String thumbnail)
	{
		this.HTMLString = HTMLString;
		this.tags = tags;
		this.link = link;
		this.thumbnail = thumbnail;
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
	
	public String getLink()
	{
		return (link);
	}
	
	public String getThumbnail()
	{
		return (thumbnail);
	}
}
