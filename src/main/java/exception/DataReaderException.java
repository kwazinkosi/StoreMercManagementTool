package exception;

public class DataReaderException extends RuntimeException {
    public enum ErrorType {
        FILE_READ_ERROR,
        PARSING_ERROR,
        THREAD_INTERRUPT,
        GENERAL_ERROR
    }

    private final ErrorType errorType;

    public DataReaderException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public DataReaderException(String message, Throwable cause, ErrorType errorType) {
        super(message, cause);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public static DataReaderException fileReadError(String filePath, Throwable cause) {
        return new DataReaderException(
            "Error reading file: " + filePath, 
            cause, 
            ErrorType.FILE_READ_ERROR
        );
    }

    public static DataReaderException parsingError(String filePath, Throwable cause) {
        return new DataReaderException(
            "Error parsing file: " + filePath, 
            cause, 
            ErrorType.PARSING_ERROR
        );
    }

    public static DataReaderException threadInterrupted(String message) {
        return new DataReaderException(
            "Thread processing interrupted: " + message, 
            ErrorType.THREAD_INTERRUPT
        );
    }

	public static Exception fileNotFound(String filePath) {
		
		return new DataReaderException(
            "File not found or not readable: " + filePath, 
            ErrorType.FILE_READ_ERROR);
	}
}