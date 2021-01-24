package csw.assignment.json.xml.converter.jackson.xml;

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
	 * Gets the serializer.
	 *
	 * @return the serializer
	 */
	StdSerializer<T> getSerializer();

	/**
	 * Gets the target type.
	 *
	 * @return the target type
	 */
	Class<T> getTargetType();

}