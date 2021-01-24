package csw.assignment.json.xml.converter;

import java.io.File;

import csw.assignment.json.xml.converter.json.MapperUtils;


/**
 * The Default implementation of {@link XMLJSONConverter} for JSON to XML
 * Converter.
 *
 * @author praveen_kumar_nr
 */
public class DefaultXMLJSONConverter implements XMLJSONConverter {

	@Override
	public boolean convertJSONtoXML(File jsonFile, File xmlFile) {
		return MapperUtils.getInstance().toXml(jsonFile, xmlFile);
	}

}
