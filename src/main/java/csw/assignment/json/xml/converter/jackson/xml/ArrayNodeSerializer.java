package csw.assignment.json.xml.converter.jackson.xml;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLStreamWriter2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

/**
 * The Customized Serializer for {@link ArrayNode}.
 * 
 * @author praveen_kumar_nr
 */
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