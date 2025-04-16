package exception;

public class TShirtServiceException extends RuntimeException {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TShirtServiceException(String message) {
        super(message);
    }

    public TShirtServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}