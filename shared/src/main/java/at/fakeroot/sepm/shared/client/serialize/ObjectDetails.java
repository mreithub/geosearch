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
	private String homepage;
	
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
	public ObjectDetails(String HTMLString, String[] tags, String link, String thumbnail, String homepage)
	{
		this.HTMLString = HTMLString;
		this.tags = tags;
		this.link = link;
		this.thumbnail = thumbnail;
		this.homepage = homepage;
	}
	
	/**
	 * @return String The HTML detail string.
	 */
	public String getHTMLString()
	{
		return (HTMLString);
	}
	
	/**
	 * @return String[] The tag String array.
	 */
	public String[] getTags()
	{
		return(tags);
	}
	
	/**
	 * @return String The link to the source.
	 */
	public String getLink()
	{
		return (link);
	}
	
	/**
	 * @return String The link to the thumbnail of the service-logo
	 */
	public String getThumbnail()
	{
		return (thumbnail);
	}
	
	/**
	 * @return String The link to the Homepage of the service
	 */
	public String getHomepage()
	{
		return (homepage);
	}
}
