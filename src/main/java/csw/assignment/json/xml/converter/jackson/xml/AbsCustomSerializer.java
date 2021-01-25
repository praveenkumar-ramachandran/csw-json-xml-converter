package csw.assignment.json.xml.converter.jackson.xml;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLStreamWriter2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;


/**
 * The Base Class for Custom Serializer.
 *
 * @author praveen_kumar_nr
 * @param <T> the generic type
 */
abstract class AbsCustomSerializer<T> extends StdSerializer<T>
	implements ICustomSerializer<T> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The q name. */
	private final QName qName;

	/**
	 * Instantiates a new abs custom serializer.
	 *
	 * @param type the type
	 */
	protected AbsCustomSerializer(Class<T> type) {
		super(type);
		this.qName = new QName(getTagName());
	}

	/**
	 * Gets the serializer.
	 *
	 * @return the serializer
	 */
	@Override
	public StdSerializer<T> getStdSerializer() {
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

	@Override
	public AbsCustomSerializer<T> getCustomSerializer() {
		return this;
	}

	@Override
	public QName getQName() {
		return this.qName;
	}

	/**
	 * Serialize.
	 *
	 * @param value     the value
	 * @param generator the gen
	 * @param provider  the provider
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void serialize(T value, JsonGenerator generator, SerializerProvider provider)
		throws IOException {
		if (value == null) {
			generator.writeNull();
			return;
		}
		ToXmlGenerator xmlGenerator = (ToXmlGenerator)generator;
		xmlGenerator.setNextName(this.getQName());
		writeValue(value, xmlGenerator, provider);
	}

	/**
	 * Write value based on the implementation.
	 *
	 * @param value        the value
	 * @param xmlGenerator the xml generator
	 * @param provider     the provider
	 * @throws IOException Signals that an I/O exception has
	 *                     occurred.
	 */
	protected abstract void writeValue(T value,
		ToXmlGenerator xmlGenerator, SerializerProvider provider)
		throws IOException;

	/**
	 * Write value.
	 *
	 * @param value     the value
	 * @param xmlWriter the xml writer
	 * @param provider  the provider
	 * @throws IOException        Signals that an I/O exception has occurred.
	 * @throws XMLStreamException the XML stream exception
	 */
	protected abstract void writeValue(T value,
		XMLStreamWriter2 xmlWriter,
		SerializerProvider provider)
		throws IOException, XMLStreamException;

}
