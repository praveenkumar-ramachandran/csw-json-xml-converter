package csw.assignment.json.xml.converter.jackson.xml;

import java.io.IOException;

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

	@Override
	protected void writeValue(TextNode value,
		ToXmlGenerator xmlGenerator, SerializerProvider provider)
		throws IOException {
		xmlGenerator.writeString(value.asText());
	}

	@Override
	protected void writeValue(TextNode value,
		XMLStreamWriter2 xmlWriter,
		ToXmlGenerator xmlGenerator,
		SerializerProvider provider)
		throws XMLStreamException, IOException {
		xmlWriter.writeCharacters(value.asText());
	}

}
