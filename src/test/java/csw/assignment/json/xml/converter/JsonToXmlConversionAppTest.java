package csw.assignment.json.xml.converter;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultComparisonFormatter;
import org.xmlunit.diff.Diff;

import csw.assignment.json.xml.converter.constants.FileType;
import lombok.extern.log4j.Log4j2;


/**
 *
 * @author praveen_kumar_nr
 */
@Log4j2
@TestMethodOrder(OrderAnnotation.class)
class JsonToXmlConversionAppTest {

	private static final String RESOURCE_DIR;

	static {
		String rootDir = Paths.get(StringUtils.EMPTY)
			.toAbsolutePath().toString();
		RESOURCE_DIR = Paths.get(rootDir
			+ File.separator + "src"
			+ File.separator + "test"
			+ File.separator + "resources")
			.toAbsolutePath().toString();
	}

	@Test
	@Order(1)
	void testNull() throws Exception {
		convertAndcompare("null");
	}

	@Test
	@Order(2)
	void testInteger() throws Exception {
		convertAndcompare("integer1");
		convertAndcompare("integer2");
		convertAndcompare("number");
	}

	@Test
	@Order(3)
	void testDouble() throws Exception {
		convertAndcompare("double1");
		convertAndcompare("double2");
	}

	@Test
	@Order(4)
	void testBigDecimal() throws Exception {
		convertAndcompare("big-decimal1");
		convertAndcompare("big-decimal2");
	}

	@Test
	@Order(5)
	void testString() throws Exception {
		convertAndcompare("string1");
		convertAndcompare("string2");
	}

	@Test
	@Order(6)
	void testBoolean() throws Exception {
		convertAndcompare("boolean1");
		convertAndcompare("boolean2");
	}

	@Test
	@Order(7)
	void testArray() throws Exception {
		convertAndcompare("array1");
		convertAndcompare("array2");
		convertAndcompare("array3");
		convertAndcompare("array4");
	}

	@Test
	@Order(8)
	void testObject() throws Exception {
		convertAndcompare("number-nested");
	}

	private void convertAndcompare(String fileName) throws Exception {
		String[] args = {
			getJsonFile(fileName).getAbsolutePath(),
			getXmlFile(fileName).getAbsolutePath()
		};
		JsonToXmlConversionApp.main(args);
		Diff diff = DiffBuilder
			.compare(Input.fromFile(getXmlFile(fileName)))
			.withTest(Input.fromFile(getSampleXmlFile(fileName)))
			.build();
		if (diff.hasDifferences()) {
			log.error(diff.toString(new DefaultComparisonFormatter()));
		}
		assertFalse(diff.hasDifferences());
	}

	private File getJsonFile(String fileName) throws Exception {
		return getFile(getInputJsonFileName(fileName));
	}

	private File getXmlFile(String fileName) throws Exception {
		return getFile(getOutputXmlFileName(fileName));
	}

	private File getSampleXmlFile(String fileName) throws Exception {
		return getFile(getSampleXmlFileName(fileName));
	}

	private File getFile(String fileName) throws Exception {
		return new File(RESOURCE_DIR + File.separator + fileName);
	}

	private String getInputJsonFileName(String fileName) {
		return "input-json-files"
			+ File.separator
			+ FileType.JSON.getFileName(fileName);
	}

	private String getOutputXmlFileName(String fileName) {
		return "output-xml-files"
			+ File.separator
			+ FileType.XML.getFileName(fileName);
	}

	private String getSampleXmlFileName(String fileName) {
		return "sample-xml-files"
			+ File.separator
			+ FileType.XML.getFileName(fileName + "-sample");
	}

}
