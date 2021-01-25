package csw.assignment.json.xml.converter.jackson.xml;

import java.io.IOException;
import java.math.BigDecimal;

import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLStreamWriter2;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;


/**
 * The Customized Serializer for {@link NumericNode}.
 * 
 * @author praveen_kumar_nr
 */
class NumericNodeSerializer extends AbsCustomSerializer<NumericNode> {

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

	@Override
	protected void writeValue(NumericNode value,
		ToXmlGenerator xmlGenerator,
		SerializerProvider provider)
		throws IOException {
		xmlGenerator.writeString(toString(value));
	}

	@Override
	protected void writeValue(NumericNode value,
		XMLStreamWriter2 xmlWriter,
		SerializerProvider provider)
		throws XMLStreamException, IOException {
		xmlWriter.writeCharacters(toString(value));
	}

	/**
	 * To string.
	 *
	 * @param value the value
	 * @return the string
	 */
	private String toString(NumericNode value) {
		if (value.isFloatingPointNumber()) {
			return new BigDecimal(value.asText())
				.setScale(6)
				.toPlainString();
		}
		return new BigDecimal(value.asText())
			.toPlainString();
	}

}
