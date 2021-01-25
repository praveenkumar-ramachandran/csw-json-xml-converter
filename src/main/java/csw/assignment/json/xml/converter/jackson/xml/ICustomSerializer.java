package csw.assignment.json.xml.converter.jackson.xml;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.databind.ser.std.StdSerializer;


/**
 * The Base Interface for Custom Serializer.
 *
 * @author praveen_kumar_nr
 * @param <T> the generic type
 */
interface ICustomSerializer<T> {

	/**
	 * Gets the serializer name.
	 *
	 * @return the serializer name
	 */
	String getSerializerName();

	/**
	 * Gets the tag name.
	 *
	 * @return the tag name
	 */
	String getTagName();

	/**
	 * Gets the std serializer.
	 *
	 * @return the std serializer
	 */
	StdSerializer<T> getStdSerializer();

	/**
	 * Gets the target type.
	 *
	 * @return the target type
	 */
	Class<T> getTargetType();

	/**
	 * Gets the custom serializer.
	 *
	 * @return the custom serializer
	 */
	AbsCustomSerializer<T> getCustomSerializer();

	/**
	 * Gets the {@link QName} for this Serializer.
	 *
	 * @return the q name
	 */
	QName getQName();

}
