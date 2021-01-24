package csw.assignment.json.xml.converter.json.xml;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLStreamWriter2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DecimalNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ShortNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import csw.assignment.json.xml.converter.exception.ConversionRuntimeException;
import lombok.extern.slf4j.Slf4j;


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

	/**
	 * The Base Interface for Custom Serializer.
	 *
	 * @author praveen_kumar_nr
	 * @param <T> the generic type
	 */
	private interface ICustomSerializer<T> {

		/**
		 * Gets the serializer name.
		 *
		 * @return the serializer name
		 */
		String getSerializerName();

		/**
		 * Gets the tag name.
		 *
		 * @return the tag name
		 */
		String getTagName();

		/**
		 * Gets the serializer.
		 *
		 * @return the serializer
		 */
		StdSerializer<T> getSerializer();

		/**
		 * Gets the target type.
		 *
		 * @return the target type
		 */
		Class<T> getTargetType();

	}

	/**
	 * The Base Class for Custom Serializer.
	 *
	 * @author praveen_kumar_nr
	 * @param <T> the generic type
	 */

	/** The Constant log. */
	@Slf4j
	private static abstract class AbsCustomSerializer<T> extends StdSerializer<T>
		implements ICustomSerializer<T> {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/**
		 * Instantiates a new abs custom serializer.
		 *
		 * @param type the type
		 */
		protected AbsCustomSerializer(Class<T> type) {
			super(type);
		}

		/**
		 * Gets the serializer.
		 *
		 * @return the serializer
		 */
		@Override
		public StdSerializer<T> getSerializer() {
			return this;
		}

		/**
		 * Gets the target type.
		 *
		 * @return the target type
		 */
		@Override
		public Class<T> getTargetType() {
			return handledType();
		}

		/**
		 * Serialize.
		 *
		 * @param value    the value
		 * @param gen      the gen
		 * @param provider the provider
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		@Override
		public void serialize(T value, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
			if (value == null) {
				gen.writeNull();
				return;
			}
			ToXmlGenerator generator = (ToXmlGenerator)gen;
			try {
				XMLStreamWriter2 writer = (XMLStreamWriter2)generator.getStaxWriter();
				writer.writeStartElement(getTagName());
				writeValue(writer, value, generator, provider);
				writer.writeEndElement();
			} catch (XMLStreamException exception) {
				String message = "Exception in " + getSerializerName()
					+ ", " + exception.getMessage();
				log.error(message);
				throw new ConversionRuntimeException(message, exception);
			}
		}

		/**
		 * Write value.
		 *
		 * @param writer    the writer
		 * @param value     the value
		 * @param generator the generator
		 * @param provider  the provider
		 * @throws XMLStreamException the XML stream exception
		 * @throws IOException        Signals that an I/O exception has
		 *                            occurred.
		 */
		protected abstract void writeValue(XMLStreamWriter2 writer, T value,
			ToXmlGenerator generator, SerializerProvider provider)
			throws XMLStreamException, IOException;

	}

	/**
	 * The Customized Serializer for {@link NumericNode}.
	 * 
	 * @author praveen_kumar_nr
	 */

	/** The Constant log. */
	@Slf4j
	private static class NumericNodeSerializer extends AbsCustomSerializer<NumericNode> {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/**
		 * Instantiates a new numeric node serializer.
		 */
		protected NumericNodeSerializer() {
			super(NumericNode.class);
		}

		/**
		 * Gets the serializer name.
		 *
		 * @return the serializer name
		 */
		@Override
		public String getSerializerName() {
			return "NumericNodeSerializer";
		}

		/**
		 * Gets the tag name.
		 *
		 * @return the tag name
		 */
		@Override
		public String getTagName() {
			return "number";
		}

		/**
		 * Write value.
		 *
		 * @param writer    the writer
		 * @param value     the value
		 * @param generator the generator
		 * @param provider  the provider
		 * @throws XMLStreamException the XML stream exception
		 */
		@Override
		protected void writeValue(XMLStreamWriter2 writer, NumericNode value,
			ToXmlGenerator generator, SerializerProvider provider)
			throws XMLStreamException {
			if (value instanceof ShortNode) {
				writer.writeInt(value.shortValue());
			} else if (value instanceof IntNode) {
				writer.writeInt(value.intValue());
			} else if (value instanceof LongNode) {
				writer.writeLong(value.longValue());
			} else if (value instanceof FloatNode) {
				writer.writeDecimal(new BigDecimal(value.floatValue()));
			} else if (value instanceof DoubleNode) {
				writer.writeDecimal(new BigDecimal(value.doubleValue()));
			} else if (value instanceof DecimalNode) {
				writer.writeDecimal(new BigDecimal(value.longValue()));
			} else if (value instanceof BigIntegerNode) {
				writer.writeDecimal(new BigDecimal(value.bigIntegerValue()));
			} else {
				String message = "Exception in " + getSerializerName()
					+ " Undefined type : "
					+ value.getClass();
				log.error(message);
				throw new ConversionRuntimeException(message);
			}
		}

	}

	/**
	 * The Customized Serializer for {@link TextNode}.
	 * 
	 * @author praveen_kumar_nr
	 */
	private static class TextNodeSerializer extends AbsCustomSerializer<TextNode> {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/**
		 * Instantiates a new text node serializer.
		 */
		protected TextNodeSerializer() {
			super(TextNode.class);
		}

		/**
		 * Gets the serializer name.
		 *
		 * @return the serializer name
		 */
		@Override
		public String getSerializerName() {
			return "TextNodeSerializer";
		}

		/**
		 * Gets the tag name.
		 *
		 * @return the tag name
		 */
		@Override
		public String getTagName() {
			return "string";
		}

		/**
		 * Write value.
		 *
		 * @param writer    the writer
		 * @param value     the value
		 * @param generator the generator
		 * @param provider  the provider
		 * @throws XMLStreamException the XML stream exception
		 */
		@Override
		protected void writeValue(XMLStreamWriter2 writer, TextNode value,
			ToXmlGenerator generator, SerializerProvider provider)
			throws XMLStreamException {
			writer.writeCharacters(value.asText());
		}

	}

	/**
	 * The Customized Serializer for {@link NullNode}.
	 * 
	 * @author praveen_kumar_nr
	 */
	private static class NullNodeSerializer extends AbsCustomSerializer<NullNode> {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/**
		 * Instantiates a new null node serializer.
		 */
		protected NullNodeSerializer() {
			super(NullNode.class);
		}

		/**
		 * Gets the serializer name.
		 *
		 * @return the serializer name
		 */
		@Override
		public String getSerializerName() {
			return "NullNodeSerializer";
		}

		/**
		 * Gets the tag name.
		 *
		 * @return the tag name
		 */
		@Override
		public String getTagName() {
			return "null";
		}

		/**
		 * Write value.
		 *
		 * @param writer    the writer
		 * @param value     the value
		 * @param generator the generator
		 * @param provider  the provider
		 * @throws XMLStreamException the XML stream exception
		 */
		@Override
		protected void writeValue(XMLStreamWriter2 writer, NullNode value,
			ToXmlGenerator generator, SerializerProvider provider)
			throws XMLStreamException {
			// do nothing
		}

	}

	/**
	 * The Customized Serializer for {@link BooleanNode}.
	 * 
	 * @author praveen_kumar_nr
	 */
	private static class BooleanNodeSerializer extends AbsCustomSerializer<BooleanNode> {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/**
		 * Instantiates a new boolean node serializer.
		 */
		protected BooleanNodeSerializer() {
			super(BooleanNode.class);
		}

		/**
		 * Gets the serializer name.
		 *
		 * @return the serializer name
		 */
		@Override
		public String getSerializerName() {
			return "BooleanNodeSerializer";
		}

		/**
		 * Gets the tag name.
		 *
		 * @return the tag name
		 */
		@Override
		public String getTagName() {
			return "boolean";
		}

		/**
		 * Write value.
		 *
		 * @param writer    the writer
		 * @param value     the value
		 * @param generator the generator
		 * @param provider  the provider
		 * @throws XMLStreamException the XML stream exception
		 */
		@Override
		protected void writeValue(XMLStreamWriter2 writer, BooleanNode value,
			ToXmlGenerator generator, SerializerProvider provider)
			throws XMLStreamException {
			writer.writeBoolean(value.asBoolean());
		}

	}

	/**
	 * The Customized Serializer for {@link ArrayNode}.
	 * 
	 * @author praveen_kumar_nr
	 */
	private static class ArrayNodeSerializer extends AbsCustomSerializer<ArrayNode> {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/**
		 * Instantiates a new array node serializer.
		 */
		protected ArrayNodeSerializer() {
			super(ArrayNode.class);
		}

		/**
		 * Gets the serializer name.
		 *
		 * @return the serializer name
		 */
		@Override
		public String getSerializerName() {
			return "ArrayNodeSerializer";
		}

		/**
		 * Gets the tag name.
		 *
		 * @return the tag name
		 */
		@Override
		public String getTagName() {
			return "array";
		}

		/**
		 * Write value.
		 *
		 * @param writer    the writer
		 * @param value     the value
		 * @param generator the generator
		 * @param provider  the provider
		 * @throws XMLStreamException the XML stream exception
		 * @throws IOException        Signals that an I/O exception has
		 *                            occurred.
		 */
		@Override
		protected void writeValue(XMLStreamWriter2 writer, ArrayNode value,
			ToXmlGenerator generator, SerializerProvider provider)
			throws XMLStreamException, IOException {
			for (JsonNode jsonNode : value) {
				generator.writeObject(jsonNode);
			}
		}

	}

	/**
	 * The Customized Serializer for {@link ObjectNode}.
	 * 
	 * @author praveen_kumar_nr
	 */
	private static class ObjectNodeSerializer extends AbsCustomSerializer<ObjectNode> {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/**
		 * Instantiates a new object node serializer.
		 */
		protected ObjectNodeSerializer() {
			super(ObjectNode.class);
		}

		/**
		 * Gets the serializer name.
		 *
		 * @return the serializer name
		 */
		@Override
		public String getSerializerName() {
			return "ObjectNodeSerializer";
		}

		/**
		 * Gets the tag name.
		 *
		 * @return the tag name
		 */
		@Override
		public String getTagName() {
			return "object";
		}

		/**
		 * Write value.
		 *
		 * @param writer    the writer
		 * @param value     the value
		 * @param generator the generator
		 * @param provider  the provider
		 * @throws XMLStreamException the XML stream exception
		 * @throws IOException        Signals that an I/O exception has
		 *                            occurred.
		 */
		@Override
		protected void writeValue(XMLStreamWriter2 writer, ObjectNode value,
			ToXmlGenerator generator, SerializerProvider provider)
			throws XMLStreamException, IOException {
			Iterator<Entry<String, JsonNode>> fields = value.fields();
			while (fields.hasNext()) {
				Entry<String, JsonNode> field = fields.next();
				generator.writeObject(field.getValue());
			}
		}

	}

}
