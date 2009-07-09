package at.fakeroot.sepm.server;

/**
 * Exception Class, thrown when the searched-for object does not exist in the database 
 * */
public class NotFoundException extends Exception {
	/// default serial version id
	private static final long serialVersionUID = 1L;

	public NotFoundException() {
		super();
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
