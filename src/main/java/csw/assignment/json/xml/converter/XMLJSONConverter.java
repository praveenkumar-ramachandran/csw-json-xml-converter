package csw.assignment.json.xml.converter;

import java.io.File;


/**
 * The Base Interface for JSON to XML Conversion.
 *
 * @author praveen_kumar_nr
 */
public interface XMLJSONConverter {

	/**
	 * Convert JSO nto XML.
	 *
	 * @param jsonFile the json file
	 * @param xmlFile  the xml file
	 * @return true, if successful
	 */
	boolean convertJSONtoXML(File jsonFile, File xmlFile);

}
