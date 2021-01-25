package csw.assignment.json.xml.converter.jackson.xml;

import java.io.IOException;

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

	@Override
	protected void writeValue(NullNode value,
		ToXmlGenerator xmlGenerator, SerializerProvider provider)
		throws IOException {
		xmlGenerator.writeNull();
	}

	@Override
	protected void writeValue(NullNode value,
		XMLStreamWriter2 xmlWriter,
		ToXmlGenerator xmlGenerator,
		SerializerProvider provider)
		throws IOException, XMLStreamException {
		// do nothing
	}

}
