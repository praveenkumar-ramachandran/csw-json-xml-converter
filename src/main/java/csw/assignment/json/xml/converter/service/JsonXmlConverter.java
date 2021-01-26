package csw.assignment.json.xml.converter.service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import csw.assignment.json.xml.converter.constants.FileType;


/**
 * The Base Interface for JSON to XML Conversion.
 *
 * @author praveen_kumar_nr
 */
public interface JsonXmlConverter {

	/**
	 * Convert JSON to XML.
	 *
	 * @param jsonFile the JSON file
	 * @param xmlFile  the XML file
	 */
	void convertJsonToXml(File jsonFile, File xmlFile);

	/**
	 * Convert JSON to XML. Create a new XML file in the
	 * same name of the given Input XML file, under the
	 * XML file directory provided.
	 *
	 * @param jsonFile the json file
	 * @param xmlFile  the xml file directory
	 */
	default void convertJsonToXmlFromXmlDir(File jsonFile, File xmlFile) {
		convertJsonToXml(jsonFile, getOutputXmlFileFromXmlDir(jsonFile, xmlFile));
	}

	/**
	 * Convert JSON to XML. Create a new XML file in the
	 * same name of the given Input XML file, under the
	 * same directory of the given JSON file.
	 *
	 * @param jsonFile the json file
	 */
	default void convertJsonToXml(File jsonFile) {
		convertJsonToXml(jsonFile, getOutputXmlFileFromJsonFile(jsonFile));
	}

	/**
	 * Convert JSON to XML.
	 * Key : JSON file
	 * Value : XML file
	 *
	 * @param jsonXmlFiles the JSON and XML files as Map
	 */
	default void convertJsonToXml(Map<File, File> jsonXmlFiles) {
		jsonXmlFiles.forEach(this::convertJsonToXml);
	}

	/**
	 * Convert JSON to XML. Create a new XML file in the
	 * same name of the given Input XML file, under the
	 * same directory of the given JSON file
	 * 
	 * @param jsonFiles the JSON files
	 */
	default void convertJsonToXml(List<File> jsonFiles) {
		convertJsonToXml(jsonFiles.stream()
			.collect(Collectors.toMap(jsonFile -> jsonFile,
				jsonFile -> getOutputXmlFileFromJsonFile(jsonFile))));
	}

	/**
	 * Gets the output XML file from JSON file.
	 *
	 * @param jsonFile the JSON file
	 * @return the output XML file from JSON file
	 */
	private File getOutputXmlFileFromJsonFile(File jsonFile) {
		Objects.requireNonNull(jsonFile, "jsonFile is Required");
		return new File(jsonFile.getPath()
			.replace(FileType.JSON.getExtension(),
				FileType.XML.getExtension()));
	}

	/**
	 * Gets the output XML file from XML directory.
	 *
	 * @param jsonFile the JSON file
	 * @param xmlFile  the XML file
	 * @return the output XML file from XML directory
	 */
	private File getOutputXmlFileFromXmlDir(File jsonFile, File xmlFile) {
		Objects.requireNonNull(jsonFile, "jsonFile is Required");
		Objects.requireNonNull(xmlFile, "xmlFile is Required");
		return new File(xmlFile.getPath()
			+ File.separator
			+ jsonFile.getName()
				.replace(FileType.JSON.getExtension(),
					FileType.XML.getExtension()));
	}

}
