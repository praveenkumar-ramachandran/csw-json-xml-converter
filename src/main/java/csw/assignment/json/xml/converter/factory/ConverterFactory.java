package csw.assignment.json.xml.converter.factory;

import csw.assignment.json.xml.converter.service.DefaultJsonXmlConverter;
import csw.assignment.json.xml.converter.service.JsonXmlConverter;

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
	public static JsonXmlConverter createXMLJSONConverter() {
		return new DefaultJsonXmlConverter();
	}

}
