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

/**
 * The Customized Serializer for {@link ObjectNode}.
 * 
 * @author praveen_kumar_nr
 */
class ObjectNodeSerializer extends AbsCustomSerializer<ObjectNode> {

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