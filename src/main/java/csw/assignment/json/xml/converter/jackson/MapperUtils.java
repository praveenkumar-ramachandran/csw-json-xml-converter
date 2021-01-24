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
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper.Builder;

import csw.assignment.json.xml.converter.exception.ConversionRuntimeException;
import csw.assignment.json.xml.converter.jackson.xml.CustomXmlModules;
import lombok.extern.slf4j.Slf4j;


/**
 * The Utility class for accessing Jackson Module for parsing JSON and XML data.
 *
 * @author praveen_kumar_nr
 */
@Slf4j
public class MapperUtils {

	/** The Constant OBJECT_MAPPER. */
	private static final ObjectMapper OBJECT_MAPPER;

	/** The Constant XML_MAPPER. */
	private static final XmlMapper XML_MAPPER;

	static {
		// json mapper
		OBJECT_MAPPER = new ObjectMapper();
		OBJECT_MAPPER.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
		OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
		// xml mapper
		Builder builder = XmlMapper.builder()
			.defaultUseWrapper(false)
			.enable(SerializationFeature.INDENT_OUTPUT);
		for (SimpleModule module : CustomXmlModules.getCustomModules()) {
			builder.addModule(module);
		}
		XML_MAPPER = builder.build();
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

}
