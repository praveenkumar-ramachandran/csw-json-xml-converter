package csw.assignment.json.xml.converter.jackson.xml;

import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLStreamWriter2;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

/**
 * The Customized Serializer for {@link BooleanNode}.
 * 
 * @author praveen_kumar_nr
 */
class BooleanNodeSerializer extends AbsCustomSerializer<BooleanNode> {

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