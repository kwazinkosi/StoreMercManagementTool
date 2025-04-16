package exception;

public class InvalidInputException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidInputException() {
        super();
    }

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputException(Throwable cause) {
        super(cause);
    }

    /**
     * Static factory methods for common input validation scenarios
     */
    public static InvalidInputException forEmptyInput(String fieldName) {
        return new InvalidInputException(fieldName + " cannot be empty.");
    }

    public static InvalidInputException forInvalidNumber(String fieldName) {
        return new InvalidInputException("Invalid " + fieldName + ". Please enter a valid number.");
    }

    public static InvalidInputException forNegativeValue(String fieldName) {
        return new InvalidInputException(fieldName + " must be a non-negative number.");
    }

    public static InvalidInputException forOutOfRange(String fieldName, Number min, Number max) {
        return new InvalidInputException(fieldName + " must be between " + min + " and " + max + ".");
    }
    
    public static InvalidInputException forInvalidValue(String fieldName) {
        return new InvalidInputException("Invalid " + fieldName + ". Please enter a valid value.");
    }
}