package csw.assignment.json.xml.converter.jackson.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;

import csw.assignment.json.xml.converter.exception.ConversionRuntimeException;


/**
 * The Class CustomXmlModules.
 *
 * @author praveen_kumar_nr
 */
public class CustomXmlModules {

	/** The Constant MAP. */
	private static final Map<Class<?>, ICustomSerializer<?>> MAP = new HashMap<>();

	private static final JacksonXmlModule JACKSON_XML_MODULE;

	static {
		JacksonXmlModule xmlModule = new JacksonXmlModule();
		xmlModule.setDefaultUseWrapper(false);
		// SimpleModule xmlModule = new SimpleModule();
		JACKSON_XML_MODULE = xmlModule;
	}

	/**
	 * Register.
	 *
	 * @param <T>              the generic type
	 * @param customSerializer the custom serializer
	 */
	private static <T> void register(ICustomSerializer<T> customSerializer) {
		MAP.put(customSerializer.getTargetType(), customSerializer);
		JACKSON_XML_MODULE.addSerializer(customSerializer.getStdSerializer());
	}

	/**
	 * Gets the custom module.
	 *
	 * @return the custom module
	 */
	public static JacksonXmlModule getCustomModule() {
		register(new NullNodeSerializer());
		register(new NumericNodeSerializer());
		register(new TextNodeSerializer());
		register(new BooleanNodeSerializer());
		register(new ArrayNodeSerializer());
		register(new ObjectNodeSerializer());
		return JACKSON_XML_MODULE;
	}

	/**
	 * Gets the custom serializer.
	 *
	 * @param <T>  the generic type
	 * @param type the type
	 * @return the custom serializer
	 */
	@SuppressWarnings("unchecked")
	static <T> Optional<ICustomSerializer<T>> getCustomSerializer(Class<T> type) {
		return MAP.entrySet().stream()
			.filter(entry -> entry.getKey().isAssignableFrom(type))
			.findFirst()
			.map(entry -> (ICustomSerializer<T>)entry.getValue());
	}

	/**
	 * Gets the custom serializer or throw.
	 *
	 * @param <T>  the generic type
	 * @param type the type
	 * @return the custom serializer or throw
	 */
	static <T> AbsCustomSerializer<T> getCustomSerializerOrThrow(Class<T> type) {
		Optional<ICustomSerializer<T>> customSerializer = CustomXmlModules
			.getCustomSerializer(type);
		if (customSerializer.isEmpty()) {
			throw new ConversionRuntimeException(
				"Serializer not found for type=" + type);
		}
		return customSerializer
			.get()
			.getCustomSerializer();
	}

}
