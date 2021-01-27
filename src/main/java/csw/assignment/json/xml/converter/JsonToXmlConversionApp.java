package csw.assignment.json.xml.converter;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import csw.assignment.json.xml.converter.constants.FileType;
import csw.assignment.json.xml.converter.factory.ConverterFactory;
import csw.assignment.json.xml.converter.service.JsonXmlConverter;
import lombok.extern.log4j.Log4j2;


/**
 * The Entry point of this XML to JSON conversion Application.
 *
 * @author praveen_kumar_nr
 */
@Log4j2
public class JsonToXmlConversionApp {

	/** The json file. */
	private final File jsonFile;

	/** The xml file. */
	private final File xmlFile;

	/** The converter. */
	private final JsonXmlConverter converter;

	/**
	 * Instantiates a new json to xml conversion app.
	 *
	 * @param jsonFilePath the json file path
	 * @param xmlFilePath  the xml file path
	 */
	private JsonToXmlConversionApp(String jsonFilePath, String xmlFilePath) {
		if (StringUtils.isEmpty(jsonFilePath)) {
			throw new IllegalArgumentException(
				"JSON file path is Required, but provided : "
					+ jsonFilePath);
		}
		if (StringUtils.isEmpty(xmlFilePath)) {
			throw new IllegalArgumentException(
				"XML file path is Required, but provided : "
					+ xmlFilePath);
		}
		this.jsonFile = getFile(jsonFilePath, FileType.JSON);
		this.xmlFile = getFile(xmlFilePath, FileType.XML);
		this.converter = ConverterFactory.createXMLJSONConverter();
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			log.info("*****************************************");
			log.info("**** STARTED : JsonToXmlConversionApp");
			log.info("*****************************************");

			if (args == null || args.length < 2) {
				throw new IllegalArgumentException(
					"Two arguments Required :"
						+ " [1] JSON file path, [2] XML file path..!!"
						+ " But provided : " + Arrays.toString(args));
			}

			String jsonFilePath = StringUtils.trimToNull(args[0]);
			String xmlFilePath = StringUtils.trimToNull(args[1]);
			log.info("Arguments Privided : ");
			log.info("jsonFilePath : {}", jsonFilePath);
			log.info("xmlFilePath  : {}", xmlFilePath);

			JsonToXmlConversionApp app = new JsonToXmlConversionApp(jsonFilePath, xmlFilePath);
			app.run();
			log.info("*****************************************");
			log.info("**** SUCCESS : JsonToXmlConversionApp");
			log.info("*****************************************");
		} catch (Exception exception) {
			log.error(exception);
			log.info("*****************************************");
			log.info("**** FAILED  : JsonToXmlConversionApp");
			log.info("*****************************************");
			throw exception;
		}
	}

	/**
	 * Run.
	 */
	private void run() {

		log.info("Running the JSON to XML for : ");
		log.info("  JSON file : {}", jsonFile);
		log.info("  XML file  : {}", xmlFile);

		if (xmlFile.isDirectory()) {
			this.converter.convertJsonToXmlFromXmlDir(jsonFile, xmlFile);
		} else {
			this.converter.convertJsonToXml(jsonFile, xmlFile);
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
		if (!file.exists() && !file.isDirectory() && !file.isFile()) {
			if (!file.isAbsolute()) {
				throw new IllegalArgumentException(
					"Invalid file path : " + filePath);
			}
			try {
				file.mkdirs();
			} catch (Exception exception) {
				throw new IllegalArgumentException(
					"Invalid file path : " + filePath);
			}
		}
		if (!file.exists()) {
			throw new IllegalArgumentException(
				"File not exists with path : " + filePath);
		}
		if (FileType.JSON.equals(fileType) && !file.exists()) {
			throw new IllegalArgumentException("File does not exists : " + filePath);
		}
		if (FileType.JSON.equals(fileType) && !file.isFile()) {
			throw new IllegalArgumentException(
				"Expected a file but provided a directory : " + filePath);
		}
		if (file.isFile() && !file.getPath().endsWith(fileType.getExtension())) {
			throw new IllegalArgumentException(
				"Expected a file with "
					+ fileType.getExtension()
					+ " extension, but provided a file : " + filePath);
		}
		return file;
	}

}
