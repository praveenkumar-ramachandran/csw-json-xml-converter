package csw.assignment.json.xml.converter;

import java.io.File;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import csw.assignment.json.xml.converter.constants.FileType;
import csw.assignment.json.xml.converter.exception.ConversionRuntimeException;
import csw.assignment.json.xml.converter.factory.ConverterFactory;
import csw.assignment.json.xml.converter.service.JsonXmlConverter;


/**
 * The Entry point of this XML to JSON conversion Application.
 *
 * @author praveen_kumar_nr
 */
public class JsonToXmlConversionApp {

	/** The json file. */
	private final File jsonFile;

	/** The xml file. */
	private final File xmlFile;

	/**
	 * Instantiates a new json to xml conversion app.
	 *
	 * @param jsonFilePath the json file path
	 * @param xmlFilePath  the xml file path
	 */
	private JsonToXmlConversionApp(String jsonFilePath, String xmlFilePath) {
		Objects.requireNonNull(jsonFilePath, "JSON file path is Required..!!");
		Objects.requireNonNull(xmlFilePath, "XML file path is Required..!!");
		this.jsonFile = getFile(jsonFilePath, FileType.JSON);
		this.xmlFile = getFile(xmlFilePath, FileType.XML);
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		if (args == null || args.length <= 0) {
			throw new IllegalArgumentException("Missing JSON file path and XML file path..!!");
		}

		String jsonFilePath = StringUtils.trimToNull(args[0]);
		String xmlFilePath = StringUtils.trimToNull(args[1]);

		JsonToXmlConversionApp app = new JsonToXmlConversionApp(jsonFilePath, xmlFilePath);
		app.run();

	}

	/**
	 * Run.
	 */
	private void run() {

		JsonXmlConverter converter = ConverterFactory.createXMLJSONConverter();
		boolean success = converter.convertJsontoXml(jsonFile, xmlFile);
		if (!success) {
			throw new ConversionRuntimeException("");
		}

	}

	/**
	 * Gets the file.
	 *
	 * @param filePath the file path
	 * @param fileType the file type
	 * @return the file
	 */
	private static File getFile(String filePath, FileType fileType) {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new IllegalArgumentException("File does not exists : " + filePath);
		}
		if (!file.isFile()) {
			throw new IllegalArgumentException(
				"Expected a file but provided a directory : " + filePath);
		}
		if (!file.toString().endsWith(fileType.getExtension())) {
			throw new IllegalArgumentException(
				"Expected a file with "
					+ fileType.getExtension()
					+ " extension, but provided a file : " + filePath);
		}
		return file;
	}

}
