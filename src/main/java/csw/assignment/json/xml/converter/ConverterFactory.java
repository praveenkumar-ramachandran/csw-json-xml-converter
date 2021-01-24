package csw.assignment.json.xml.converter;

/**
 * A factory for creating Converter objects.
 *
 * @author praveen_kumar_nr
 */
public class ConverterFactory {

	/**
	 * Instantiates a new converter factory.
	 */
	private ConverterFactory() {
		// use static methods instead
	}

	/**
	 * Creates a new Converter object.
	 *
	 * @return the XMLJSON converter
	 */
	public static XMLJSONConverter createXMLJSONConverter() {
		return new DefaultXMLJSONConverter();
	}

}
