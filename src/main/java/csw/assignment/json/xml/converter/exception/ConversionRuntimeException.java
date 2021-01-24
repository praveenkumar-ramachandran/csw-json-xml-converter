package csw.assignment.json.xml.converter.exception;

/**
 * The Class ConversionRuntimeException.
 *
 * @author praveen_kumar_nr
 */
public class ConversionRuntimeException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new conversion runtime exception.
	 *
	 * @param message the message
	 */
	public ConversionRuntimeException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new conversion runtime exception.
	 *
	 * @param throwable the throwable
	 */
	public ConversionRuntimeException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Instantiates a new conversion runtime exception.
	 *
	 * @param message   the message
	 * @param throwable the throwable
	 */
	public ConversionRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
