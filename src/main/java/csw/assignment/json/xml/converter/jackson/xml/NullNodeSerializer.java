package csw.assignment.json.xml.converter.jackson.xml;

import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLStreamWriter2;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

/**
 * The Customized Serializer for {@link NullNode}.
 * 
 * @author praveen_kumar_nr
 */
class NullNodeSerializer extends AbsCustomSerializer<NullNode> {

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