package csw.assignment.json.xml.converter.jackson.xml;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLStreamWriter2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import csw.assignment.json.xml.converter.exception.ConversionRuntimeException;
import lombok.extern.log4j.Log4j2;


/**
 * The Customized Serializer for {@link ArrayNode}.
 * 
 * @author praveen_kumar_nr
 */
@Log4j2
class ArrayNodeSerializer extends AbsCustomSerializer<ArrayNode> {

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

	@Override
	protected void writeValue(ArrayNode value,
		ToXmlGenerator xmlGenerator, SerializerProvider provider)
		throws IOException {
		try {
			XMLStreamWriter2 xmlWriter = (XMLStreamWriter2)xmlGenerator
				.getStaxWriter();
			xmlWriter.writeStartElement(getTagName());
			writeValue(value, xmlWriter, provider);
			xmlWriter.writeEndElement();
		} catch (XMLStreamException exception) {
			String message = "Exception in " + getSerializerName()
				+ ", " + exception.getMessage();
			log.error(message);
			throw new ConversionRuntimeException(message, exception);
		}
	}

	@Override
	protected void writeValue(ArrayNode value,
		XMLStreamWriter2 xmlWriter,
		SerializerProvider provider)
		throws IOException, XMLStreamException {
		for (JsonNode jsonNode : value) {
			writeValueByType(jsonNode.getClass(), jsonNode,
				xmlWriter, provider);
		}
	}

	/**
	 * Write value by type.
	 *
	 * @param <T>          the generic type
	 * @param type         the type
	 * @param jsonNode     the json node
	 * @param xmlWriter    the xml writer
	 * @param xmlGenerator the xml generator
	 * @param provider     the provider
	 * @throws IOException        Signals that an I/O exception has occurred.
	 * @throws XMLStreamException the XML stream exception
	 */
	@SuppressWarnings("unchecked")
	private <T> void writeValueByType(Class<T> type,
		JsonNode jsonNode, XMLStreamWriter2 xmlWriter,
		SerializerProvider provider)
		throws IOException, XMLStreamException {
		AbsCustomSerializer<T> serializer = CustomXmlModules
			.getCustomSerializerOrThrow(type);
		xmlWriter.writeStartElement(serializer.getTagName());
		serializer.writeValue((T)jsonNode,
			xmlWriter, provider);
		xmlWriter.writeEndElement();
	}

}
