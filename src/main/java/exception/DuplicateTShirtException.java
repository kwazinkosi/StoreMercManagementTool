package exception;
public class DuplicateTShirtException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateTShirtException(String message) {
        super(message);
    }
}

