package csw.assignment.json.xml.converter.jackson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import csw.assignment.json.xml.converter.exception.ConversionRuntimeException;
import csw.assignment.json.xml.converter.jackson.xml.CustomXmlModules;
import lombok.extern.log4j.Log4j2;


/**
 * The Utility class for accessing Jackson Module for parsing JSON and XML data.
 *
 * @author praveen_kumar_nr
 */
@Log4j2
public class MapperUtils {

	/** The Constant OBJECT_MAPPER. */
	private static final ObjectMapper OBJECT_MAPPER;

	/** The Constant XML_MAPPER. */
	private static final XmlMapper XML_MAPPER;

	static {

		// System.setProperty(DOMImplementationRegistry.PROPERTY,
		// "com.sun.org.apache.xerces.internal.dom.DOMImplementationSourceImpl");

		// json mapper
		OBJECT_MAPPER = new ObjectMapper();
		OBJECT_MAPPER.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
		OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);

		// xml mapper
		// Builder builder = XmlMapper.builder()
		// .defaultUseWrapper(false)
		// .enable(SerializationFeature.INDENT_OUTPUT)
		// .configure(SerializationFeature.INDENT_OUTPUT, true)
		// .addModule(CustomXmlModules.getCustomModule());
		// XML_MAPPER = builder.build();

		// XMLInputFactory inputFactory = new WstxInputFactory();
		// inputFactory.setProperty(WstxInputProperties.P_MAX_ATTRIBUTE_SIZE,
		// 32000);
		// XMLOutputFactory outputFactory = new WstxOutputFactory();
		// outputFactory.setProperty(WstxOutputProperties.P_OUTPUT_CDATA_AS_TEXT,
		// true);
		// XmlFactory xmlFactory = new XmlFactory(inputFactory, outputFactory);
		// XML_MAPPER = new XmlMapper(xmlFactory,
		// CustomXmlModules.getCustomModule());

		XML_MAPPER = new XmlMapper(CustomXmlModules.getCustomModule());
		// XML_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	/**
	 * Instantiates a new mapper utils.
	 */
	private MapperUtils() {
		// use getInstance() method instead
	}

	/**
	 * Gets the single instance of MapperUtils.
	 *
	 * @return single instance of MapperUtils
	 */
	public static MapperUtils getInstance() {
		return MapperUtilsFactory.MAPPER_UTILS;
	}

	/**
	 * A factory for creating MapperUtils objects.
	 */
	private static class MapperUtilsFactory {

		/** The Constant MAPPER_UTILS. */
		private static final MapperUtils MAPPER_UTILS = new MapperUtils();

	}

	/**
	 * To json.
	 *
	 * @param <T>   the generic type
	 * @param value the value
	 * @return the optional
	 */
	public <T> Optional<String> toJson(T value) {
		try {
			return Optional.ofNullable(OBJECT_MAPPER.writeValueAsString(value));
		} catch (JsonProcessingException exception) {
			log.error("Unable to produce JSON from {}, reason : {}",
				value, exception.getMessage());
			throw new ConversionRuntimeException(exception);
		}
	}

	/**
	 * To json node.
	 *
	 * @param jsonFile the json file
	 * @return the optional
	 */
	public Optional<JsonNode> toJsonNode(File jsonFile) {
		try {
			return Optional.ofNullable(OBJECT_MAPPER.readTree(jsonFile));
		} catch (IOException exception) {
			log.error("Unable to produce JSON Node from {}, reason : {}",
				jsonFile, exception.getMessage());
			throw new ConversionRuntimeException(exception);
		}
	}

	/**
	 * To json node.
	 *
	 * @param jsonFile the json file
	 * @return the optional
	 */
	public Optional<JsonNode> toJsonNode(InputStream jsonFile) {
		try {
			return Optional.ofNullable(OBJECT_MAPPER.readTree(jsonFile));
		} catch (IOException exception) {
			log.error("Unable to produce JSON Node from {}, reason : {}",
				jsonFile, exception.getMessage());
			throw new ConversionRuntimeException(exception);
		}
	}

	/**
	 * To xml.
	 *
	 * @param jsonFile the json file
	 * @param xmlFile  the xml file
	 * @return true, if successful
	 */
	public boolean toXml(File jsonFile, File xmlFile) {
		return toJsonNode(jsonFile).map(jsonNode -> {
			try {
				XML_MAPPER.writeValue(xmlFile, jsonNode);
				return true;
			} catch (IOException exception) {
				log.error("Unable to produce XML from JSON Node {}, reason : {}",
					jsonNode, exception.getMessage());
				throw new ConversionRuntimeException(exception);
			}
		}).orElse(false);
	}

	/**
	 * To xml.
	 *
	 * @param jsonFile the json file
	 * @return the optional
	 */
	public Optional<String> toXml(File jsonFile) {
		return toJsonNode(jsonFile).map(jsonNode -> {
			try {
				return XML_MAPPER.writeValueAsString(jsonNode);
			} catch (IOException exception) {
				log.error("Unable to produce XML from JSON Node {}, reason : {}",
					jsonNode, exception.getMessage());
				throw new ConversionRuntimeException(exception);
			}
		});
	}

}
