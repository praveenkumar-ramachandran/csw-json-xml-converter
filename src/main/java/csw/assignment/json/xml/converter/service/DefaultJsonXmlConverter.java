package csw.assignment.json.xml.converter.service;

import java.io.File;
import java.util.Optional;

import csw.assignment.json.xml.converter.exception.ConversionRuntimeException;
import csw.assignment.json.xml.converter.jackson.MapperUtils;
import csw.assignment.json.xml.converter.jackson.XmlUtils;
import lombok.extern.log4j.Log4j2;


/**
 * The Default implementation of {@link JsonXmlConverter} for JSON to XML
 * Converter.
 *
 * @author praveen_kumar_nr
 */
@Log4j2
public class DefaultJsonXmlConverter implements JsonXmlConverter {

	/** The mapper utils. */
	private final MapperUtils mapperUtils = MapperUtils.getInstance();

	/** The xml utils. */
	private final XmlUtils xmlUtils = XmlUtils.getInstance();

	@Override
	public void convertJsonToXml(File jsonFile, File xmlFile) {
		Optional<String> xmlOpt = mapperUtils.toXml(jsonFile);
		if (xmlOpt.isEmpty()) {
			throw new ConversionRuntimeException(
				"Generated an empty XML content for" + jsonFile.toString());
		}
		xmlOpt.ifPresent(xml -> {
			xmlUtils.format(xml, xmlFile);
		});
		log.info("XML file generated into : {}", xmlFile);
	}

}
