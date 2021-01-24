package csw.assignment.json.xml.converter.jackson.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.module.SimpleModule;


/**
 * The Class CustomXmlModules.
 *
 * @author praveen_kumar_nr
 */
public class CustomXmlModules {

	/** The Constant MAP. */
	private static final Map<Class<?>, ICustomSerializer<?>> MAP = new HashMap<>();

	/**
	 * Register.
	 *
	 * @param <T>              the generic type
	 * @param customSerializer the custom serializer
	 */
	private static <T> void register(ICustomSerializer<T> customSerializer) {
		MAP.put(customSerializer.getTargetType(), customSerializer);
	}

	/**
	 * New simple module.
	 *
	 * @param <T>              the generic type
	 * @param customSerializer the custom serializer
	 * @return the simple module
	 */
	private static <T> SimpleModule newSimpleModule(ICustomSerializer<T> customSerializer) {
		return new SimpleModule(getModuleName(customSerializer))
			.addSerializer(customSerializer.getSerializer());
	}

	/**
	 * Gets the module name.
	 *
	 * @return the module name
	 */
	private static <T> String getModuleName(ICustomSerializer<T> customSerializer) {
		return "CustomXmlModules." + customSerializer.getSerializerName();
	}

	/**
	 * Gets the custom modules.
	 *
	 * @return the custom modules
	 */
	public static List<SimpleModule> getCustomModules() {
		register(new NullNodeSerializer());
		register(new NumericNodeSerializer());
		register(new TextNodeSerializer());
		register(new BooleanNodeSerializer());
		register(new ArrayNodeSerializer());
		register(new ObjectNodeSerializer());
		return MAP.values().stream()
			.map(serializer -> newSimpleModule(serializer))
			.collect(Collectors.toList());
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
			.map(customSer -> (ICustomSerializer<T>)customSer);
	}

}
