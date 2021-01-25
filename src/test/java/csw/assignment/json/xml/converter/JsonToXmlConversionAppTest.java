package csw.assignment.json.xml.converter;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
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

	@Nested
	class NullTypesTest {

		@Test
		@Order(001)
		void testNull() throws Exception {
			convertAndcompare("null");
		}

		@Test
		@Order(001)
		void testNullNested() throws Exception {
			convertAndcompare("null-nested");
		}

	}

	@Nested
	class NumericTypesTest {

		@Test
		@Order(201)
		void testInteger1() throws Exception {
			convertAndcompare("integer1");
		}

		@Test
		@Order(202)
		void testInteger2() throws Exception {
			convertAndcompare("integer2");
		}

		@Test
		@Order(211)
		void testNumber() throws Exception {
			convertAndcompare("number");
		}

		@Test
		@Order(212)
		void testNumberNested() throws Exception {
			convertAndcompare("number-nested");
		}

		@Test
		@Order(221)
		void testDouble1() throws Exception {
			convertAndcompare("double1");
		}

		@Test
		@Order(222)
		void testDouble2() throws Exception {
			convertAndcompare("double2");
		}

		@Test
		@Order(231)
		void testBigDecimal1() throws Exception {
			convertAndcompare("big-decimal1");
		}

		@Test
		@Order(232)
		void testBigDecimal2() throws Exception {
			convertAndcompare("big-decimal2");
		}

	}

	@Nested
	class StringTypesTest {

		@Test
		@Order(301)
		void testString1() throws Exception {
			convertAndcompare("string1");
		}

		@Test
		@Order(302)
		void testString2() throws Exception {
			convertAndcompare("string2");
		}

		@Test
		@Order(303)
		void testStringNested() throws Exception {
			convertAndcompare("string-nested");
		}

	}

	@Nested
	class BooleanTypesTest {

		@Test
		@Order(401)
		void testBoolean1() throws Exception {
			convertAndcompare("boolean1");
		}

		@Test
		@Order(402)
		void testBoolean() throws Exception {
			convertAndcompare("boolean2");
		}

		@Test
		@Order(403)
		void testBooleanNested() throws Exception {
			convertAndcompare("boolean-nested");
		}

	}

	@Nested
	class ArrayTypesTest {

		@Test
		@Order(501)
		void testArray1() throws Exception {
			convertAndcompare("array1");
		}

		@Test
		@Order(502)
		void testArray2() throws Exception {
			convertAndcompare("array2");
		}

		@Test
		@Order(503)
		void testArray3() throws Exception {
			convertAndcompare("array3");
		}

		@Test
		@Order(504)
		void testArray4() throws Exception {
			convertAndcompare("array4");
		}

		@Test
		@Order(520)
		void testArraySingle() throws Exception {
			convertAndcompare("array-single");
		}

		@Test
		@Order(530)
		void testArrayNested() throws Exception {
			convertAndcompare("array-nested");
		}

	}

	@Nested
	class ObjectTypesTest {

		@Test
		@Order(601)
		void testObject() throws Exception {
			convertAndcompare("object");
		}

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
