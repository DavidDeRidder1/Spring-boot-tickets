package exceptions;

public class SportNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public SportNotFoundException(long id) {
	    super(String.format("Sport met id %s niet gevonden", id));
	}

}
