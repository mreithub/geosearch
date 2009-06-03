import java.io.Serializable;



/**
 * @author Anca Cismasiu
 * Class representing object properties
 * */

public class Property implements Serializable  {

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
