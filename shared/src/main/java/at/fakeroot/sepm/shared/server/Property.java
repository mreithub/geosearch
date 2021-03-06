package at.fakeroot.sepm.shared.server;

import java.io.Serializable;

import at.fakeroot.sepm.shared.client.serialize.GeoObject;

/**
 * Class representing object properties
 * @author AC
 * */

public class Property implements Serializable {
	/// default serial version id
	private static final long serialVersionUID = 1L;

	private String name;
	private String value;

	/**
	 * Constructor
	 * @param name String property name
	 * @param value String property value
	 * */	
	public Property(String name, String value){
		this.name=name;
		// maximum value length: 1000
		this.value = GeoObject.truncate(value, 1000);
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

	public String toString() {
		return "name: "+name+", value: "+value;
	}


}
