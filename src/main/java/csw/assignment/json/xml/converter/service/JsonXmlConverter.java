package csw.assignment.json.xml.converter.service;

import java.io.File;


/**
 * The Base Interface for JSON to XML Conversion.
 *
 * @author praveen_kumar_nr
 */
public interface JsonXmlConverter {

	/**
	 * Convert JSO nto XML.
	 *
	 * @param jsonFile the json file
	 * @param xmlFile  the xml file
	 * @return true, if successful
	 */
	boolean convertJsontoXml(File jsonFile, File xmlFile);

}
