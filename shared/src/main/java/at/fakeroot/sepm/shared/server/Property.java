package at.fakeroot.sepm.shared.server;

/**
 * @author Anca Cismasiu
 * Class representing object properties
 * */

public class Property {

	private String name;
	private String value;

	/**
	 * Constructor
	 * @param name String property name
	 * @param value String property value
	 * */	
	public Property(String name, String value){
		this.name=name;
		this.value=value;
	}

	/**
	 * Getter for property name
	 * @return String property name
	 * */
	public String getName() {
		return name;
	}

	/**
	 * Getter for property value
	 * @return String property value
	 * */
	public String getValue() {
		return value;
	}



}
