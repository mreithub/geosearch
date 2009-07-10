package at.fakeroot.sepm.server;

/**
 * Class representing a Service with all the necessary properties to be stored in and read from the Database
 * @author AC
 * */

public class DBService {

	private int svcId;
	private String name;
	private String title;
	private String homepage;
	private String description;
	private int sTypeId;
	private String bubbleHTML;
	private String[] tags;
	private String thumbnail;

	/**
	 * Constructor 
	 * @param _svc_id the unique service id
	 * @param _name unique service name, used by crawler to get svd_id (eg. wikipedia.de) 
	 * @param _title title, as shown to the user (eg. wikipedia)
	 * @param _homepage  service homepage
	 * @param _description servce description
	 * @param _sType_id teh service type id (eg. Photos, Events, etc)
	 * @param _bubbleHTML String the HTML to be displayed in the DetailView
	 * @param _thumbnail String link to the thumbnail of the service-logo
	 * */
	public DBService(int svcId, String name, String title, String homepage, String description, int sTypeId, String bubbleHTML, String thumbnail){
		this.svcId = svcId;
		this.name = name;
		this.title = title;
		this.homepage = homepage;
		this.description = description;
		this.sTypeId = sTypeId;
		this.bubbleHTML = bubbleHTML;
		this.thumbnail = thumbnail;
		this.tags = new String[0];
	}

	
	/**
	 * Getter for the service id
	 * @return service id
	 * */
	@Deprecated
	public int getSvc_id() {
		return svcId;
	}
	
	/**
	 * Getter for the service ID
	 * @return service id
	 */
	public int getSvcId() {
		return svcId;
	}

	/**
	 * Getter for the service name
	 * @return service name
	 * */
	public String getName() {
		return name;
	}

	/**
	 * Getter for the service title
	 * @return service title
	 * */
	public String getTitle() {
		return title;
	}

	/**
	 * Getter for the service homepage link
	 * @return service homepage link
	 * */
	public String getHomepage() {
		return homepage;
	}

	/**
	 * Getter for the service description
	 * @return service description
	 * */
	public String getDescription() {
		return description;
	}

	/**
	 * Getter for the service type id
	 * @return service type id
	 * @deprecated use getSTypeId() instead
	 * */
	@Deprecated
	public int getSType_id() {
		return sTypeId;
	}

	/**
	 * Getter for the service type id
	 * @return service type id
	 */
	public int getSTypeId() {
		return sTypeId;
	}

	/**
	 * Getter for the DetailView HTML
	 * @return HTML String
	 * */
	public String getBubbleHTML() {
		return bubbleHTML;
	}
	
	/**
	 * Getter for the thumbnail
	 * @return String link to the thumbnail service-logo
	 */
	public String getThumbnail()
	{
		return (thumbnail);
	}
	
	/**
	 * Getter for the service tags
	 * @return service tags
	 * */
	public String[] getTags() {
		return tags;
	}

	/**
	 * Setter for the service tags
	 * @param service tags
	 * */
	public void setTags(String[] tags) {
		this.tags = tags;
	}
	
	
	
	
}
