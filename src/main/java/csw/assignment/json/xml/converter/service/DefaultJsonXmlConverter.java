package csw.assignment.json.xml.converter.service;

import java.io.File;

import csw.assignment.json.xml.converter.jackson.MapperUtils;


/**
 * The Default implementation of {@link JsonXmlConverter} for JSON to XML
 * Converter.
 *
 * @author praveen_kumar_nr
 */
public class DefaultJsonXmlConverter implements JsonXmlConverter {

	@Override
	public boolean convertJsontoXml(File jsonFile, File xmlFile) {
		return MapperUtils.getInstance()
			.toXml(jsonFile, xmlFile);
	}

}
