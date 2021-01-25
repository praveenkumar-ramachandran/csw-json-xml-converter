package csw.assignment.json.xml.converter.jackson.xml;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLStreamWriter2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import csw.assignment.json.xml.converter.exception.ConversionRuntimeException;
import lombok.extern.log4j.Log4j2;


/**
 * The Customized Serializer for {@link ObjectNode}.
 * 
 * @author praveen_kumar_nr
 */
@Log4j2
class ObjectNodeSerializer extends AbsCustomSerializer<ObjectNode> {

	private static final String ATTRIBUTE_NAME = "name";

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

	@Override
	protected void writeValue(ObjectNode value,
		ToXmlGenerator xmlGenerator, SerializerProvider provider)
		throws IOException {
		try {
			XMLStreamWriter2 xmlWriter = (XMLStreamWriter2)xmlGenerator
				.getStaxWriter();
			writeValue(value, xmlWriter, xmlGenerator, provider);
		} catch (XMLStreamException exception) {
			String message = "Exception in " + getSerializerName()
				+ ", " + exception.getMessage();
			log.error(message);
			throw new ConversionRuntimeException(message, exception);
		}
	}

	@Override
	protected void writeValue(ObjectNode value,
		XMLStreamWriter2 xmlWriter,
		ToXmlGenerator xmlGenerator,
		SerializerProvider provider)
		throws IOException, XMLStreamException {
		xmlWriter.writeStartElement(getTagName());
		Iterator<Entry<String, JsonNode>> fields = value.fields();
		while (fields.hasNext()) {
			Entry<String, JsonNode> field = fields.next();
			JsonNode jsonNode = field.getValue();
			String attributeName = field.getKey();
			if (jsonNode != null) {
				serializeType(jsonNode.getClass(),
					attributeName, jsonNode,
					xmlWriter, xmlGenerator, provider);
			} else {
				xmlGenerator.writeNullField(attributeName);
			}
		}
		xmlWriter.writeEndElement();
	}

	/**
	 * Serialize type.
	 *
	 * @param <T>            the generic type
	 * @param type           the type
	 * @param attributeValue the attribute value
	 * @param value          the value
	 * @param xmlWriter      the xml writer
	 * @param xmlGenerator   the xml generator
	 * @param provider       the provider
	 * @throws IOException        Signals that an I/O exception has occurred.
	 * @throws XMLStreamException the XML stream exception
	 */
	@SuppressWarnings("unchecked")
	private <T> void serializeType(Class<T> type,
		String attributeValue, JsonNode value,
		XMLStreamWriter2 xmlWriter, ToXmlGenerator xmlGenerator,
		SerializerProvider provider)
		throws IOException, XMLStreamException {
		AbsCustomSerializer<T> serializer = CustomXmlModules
			.getCustomSerializerOrThrow(type);
		xmlGenerator.setNextName(serializer.getQName());
		xmlGenerator.writeStartObject();
		xmlGenerator.setNextIsAttribute(true);
		xmlGenerator.writeFieldName(ATTRIBUTE_NAME);
		xmlGenerator.writeString(attributeValue);
		serializer.writeValue((T)value, xmlWriter,
			xmlGenerator, provider);
		xmlGenerator.setNextIsAttribute(false);
		xmlGenerator.writeEndObject();
	}

}
