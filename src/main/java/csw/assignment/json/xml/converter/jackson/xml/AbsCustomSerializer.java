package csw.assignment.json.xml.converter.jackson.xml;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLStreamWriter2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import csw.assignment.json.xml.converter.exception.ConversionRuntimeException;
import lombok.extern.slf4j.Slf4j;


/**
 * The Base Class for Custom Serializer.
 *
 * @author praveen_kumar_nr
 * @param <T> the generic type
 */
@Slf4j
abstract class AbsCustomSerializer<T> extends StdSerializer<T>
	implements ICustomSerializer<T> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new abs custom serializer.
	 *
	 * @param type the type
	 */
	protected AbsCustomSerializer(Class<T> type) {
		super(type);
	}

	/**
	 * Gets the serializer.
	 *
	 * @return the serializer
	 */
	@Override
	public StdSerializer<T> getSerializer() {
		return this;
	}

	/**
	 * Gets the target type.
	 *
	 * @return the target type
	 */
	@Override
	public Class<T> getTargetType() {
		return handledType();
	}

	/**
	 * Serialize.
	 *
	 * @param value    the value
	 * @param gen      the gen
	 * @param provider the provider
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void serialize(T value, JsonGenerator gen, SerializerProvider provider)
		throws IOException {
		if (value == null) {
			gen.writeNull();
			return;
		}
		ToXmlGenerator generator = (ToXmlGenerator)gen;
		try {
			XMLStreamWriter2 writer = (XMLStreamWriter2)generator.getStaxWriter();
			writer.writeStartElement(getTagName());
			writeValue(writer, value, generator, provider);
			writer.writeEndElement();
		} catch (XMLStreamException exception) {
			String message = "Exception in " + getSerializerName()
				+ ", " + exception.getMessage();
			log.error(message);
			throw new ConversionRuntimeException(message, exception);
		}
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
	protected abstract void writeValue(XMLStreamWriter2 writer, T value,
		ToXmlGenerator generator, SerializerProvider provider)
		throws XMLStreamException, IOException;

}
