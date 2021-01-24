package csw.assignment.json.xml.converter.jackson.xml;

import java.math.BigDecimal;

import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLStreamWriter2;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.node.DecimalNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ShortNode;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import csw.assignment.json.xml.converter.exception.ConversionRuntimeException;
import lombok.extern.slf4j.Slf4j;


/**
 * The Customized Serializer for {@link NumericNode}.
 * 
 * @author praveen_kumar_nr
 */
@Slf4j
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
	protected void writeValue(XMLStreamWriter2 writer, NumericNode value,
		ToXmlGenerator generator, SerializerProvider provider)
		throws XMLStreamException {
		if (value instanceof ShortNode) {
			writer.writeInt(value.shortValue());
		} else if (value instanceof IntNode) {
			writer.writeInt(value.intValue());
		} else if (value instanceof LongNode) {
			writer.writeLong(value.longValue());
		} else if (value instanceof FloatNode) {
			writer.writeDecimal(new BigDecimal(value.floatValue()));
		} else if (value instanceof DoubleNode) {
			writer.writeDecimal(new BigDecimal(value.doubleValue()));
		} else if (value instanceof DecimalNode) {
			writer.writeDecimal(new BigDecimal(value.longValue()));
		} else if (value instanceof BigIntegerNode) {
			writer.writeDecimal(new BigDecimal(value.bigIntegerValue()));
		} else {
			String message = "Exception in " + getSerializerName()
				+ " Undefined type : "
				+ value.getClass();
			log.error(message);
			throw new ConversionRuntimeException(message);
		}
	}

}
