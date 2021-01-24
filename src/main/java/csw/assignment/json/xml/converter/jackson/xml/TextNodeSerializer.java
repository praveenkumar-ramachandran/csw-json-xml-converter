package csw.assignment.json.xml.converter.jackson.xml;

import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLStreamWriter2;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

/**
 * The Customized Serializer for {@link TextNode}.
 * 
 * @author praveen_kumar_nr
 */
class TextNodeSerializer extends AbsCustomSerializer<TextNode> {

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