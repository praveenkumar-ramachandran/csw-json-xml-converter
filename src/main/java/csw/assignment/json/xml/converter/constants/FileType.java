package csw.assignment.json.xml.converter.constants;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * Types of files based on file format or extension.
 *
 * @author praveen_kumar_nr
 */
@AllArgsConstructor
public enum FileType {

	/** The json. */
	JSON(".json"),

	/** The xml. */
	XML(".xml");

	/**
	 * Gets the extension.
	 *
	 * @return the extension
	 */
	@Getter
	private String extension;

	/**
	 * Gets the file name.
	 *
	 * @param fileName the file name
	 * @return the file name
	 */
	public String getFileName(String fileName) {
		Objects.requireNonNull(fileName, "fileName is Required..!!");
		return fileName + this.extension;
	}

}
