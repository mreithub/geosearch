package at.fakeroot.sepm.server;

/**
 * @author Anca Cismasiu
 * 
 * */

public class DBService {

	private int svc_id;
	private String name;
	private String title;
	private String homepage;
	private String description;
	private int sType_id;
	private String bubbleHTML;
	
	public DBService(int _svc_id, String _name, String _title, String _homepage, String _description, int _sType_id, String _bubbleHTML ){
		svc_id=_svc_id;
		name=_name;
		title=_title;
		homepage=_homepage;
		description=_description;
		sType_id=_sType_id;
		bubbleHTML=_bubbleHTML;
	}

	public int getSvc_id() {
		return svc_id;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public String getHomepage() {
		return homepage;
	}

	public String getDescription() {
		return description;
	}

	public int getSType_id() {
		return sType_id;
	}

	public String getBubbleHTML() {
		return bubbleHTML;
	}
	
	
	
	
}
