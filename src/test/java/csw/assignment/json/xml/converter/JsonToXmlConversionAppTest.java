package csw.assignment.json.xml.converter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
	class InvalidArgumentsTest {

		@Test
		@Order(001)
		void testNullArgs() throws Exception {
			String[] args = null;
			convertAndAssertThrows(args);
		}

		@Test
		@Order(002)
		void testTwoNullArgs() throws Exception {
			String[] args = new String[] {
				null, null
			};
			convertAndAssertThrows(args);
		}

		@Test
		@Order(03)
		void testNullArgsAt0() throws Exception {
			String[] args = new String[] {
				null, getXmlFilePath("null")
			};
			convertAndAssertThrows(args);
		}

		@Test
		@Order(004)
		void testNullArgsAt1() throws Exception {
			String[] args = new String[] {
				getJsonFilePath("null"), null
			};
			convertAndAssertThrows(args);
		}

		@Test
		@Order(011)
		void testEmptyArgs() throws Exception {
			String[] args = new String[] {};
			convertAndAssertThrows(args);
		}

		@Test
		@Order(012)
		void testTwoEmptyArgs() throws Exception {
			String[] args = new String[] {
				StringUtils.EMPTY, StringUtils.EMPTY
			};
			convertAndAssertThrows(args);
		}

		@Test
		@Order(013)
		void testEmptyArgAt0() throws Exception {
			String[] args = new String[] {
				StringUtils.EMPTY, getXmlFilePath("null")
			};
			convertAndAssertThrows(args);
		}

		@Test
		@Order(034)
		void testEmptyArgAt1() throws Exception {
			String[] args = new String[] {
				getJsonFilePath("null"), StringUtils.EMPTY
			};
			convertAndAssertThrows(args);
		}

		@Test
		@Order(041)
		void testTwoInvalidFiles() throws Exception {
			String[] args = new String[] {
				"invalid-file-path",
				"invalid-file-path"
			};
			convertAndAssertThrows(args);
		}

		@Test
		@Order(042)
		void testInvalidFilesAt0() throws Exception {
			String[] args = new String[] {
				"invalid-file-path",
				getXmlFilePath("null")
			};
			convertAndAssertThrows(args);
		}

		@Test
		@Order(043)
		void testInvalidFilesAt1() throws Exception {
			String[] args = new String[] {
				getJsonFilePath("null"),
				"invalid-file-path"
			};
			convertAndAssertThrows(args);
		}

		@Test
		@Order(051)
		void testTwoInvalidFileFormat() throws Exception {
			String[] args = new String[] {
				getXmlFilePath("null"),
				getJsonFilePath("null")
			};
			convertAndAssertThrows(args);
		}

		@Test
		@Order(052)
		void testInvalidFileFormatAt0() throws Exception {
			String[] args = new String[] {
				getXmlFilePath("null"),
				getXmlFilePath("null")
			};
			convertAndAssertThrows(args);
		}

		@Test
		@Order(053)
		void testInvalidFileFormatAt1() throws Exception {
			String[] args = new String[] {
				getJsonFilePath("null"),
				getJsonFilePath("null")
			};
			convertAndAssertThrows(args);
		}

	}

	@Nested
	class ValidArgumentsTest {

		@Test
		@Order(001)
		void testTwoArgsAsFile() throws Exception {
			convertAndAssert("object", "object");
		}

		@Test
		@Order(001)
		void testArg0AsFileArg1AsDir() throws Exception {
			String[] args = new String[] {
				getJsonFilePath("object"),
				getXmlFileDir("object")
			};
			convertAndAssert(args, "object");
		}

	}

	@Nested
	class NullTypesTest {

		@Test
		@Order(001)
		void testNull() throws Exception {
			convertAndAssert("null");
		}

		@Test
		@Order(001)
		void testNullNested() throws Exception {
			convertAndAssert("null-nested");
		}

	}

	@Nested
	class NumericTypesTest {

		@Test
		@Order(201)
		void testInteger1() throws Exception {
			convertAndAssert("integer1");
		}

		@Test
		@Order(202)
		void testInteger2() throws Exception {
			convertAndAssert("integer2");
		}

		@Test
		@Order(211)
		void testNumber() throws Exception {
			convertAndAssert("number");
		}

		@Test
		@Order(212)
		void testNumberNested() throws Exception {
			convertAndAssert("number-nested");
		}

		@Test
		@Order(221)
		void testDouble1() throws Exception {
			convertAndAssert("double1");
		}

		@Test
		@Order(222)
		void testDouble2() throws Exception {
			convertAndAssert("double2");
		}

		@Test
		@Order(231)
		void testBigDecimal1() throws Exception {
			convertAndAssert("big-decimal1");
		}

		@Test
		@Order(232)
		void testBigDecimal2() throws Exception {
			convertAndAssert("big-decimal2");
		}

	}

	@Nested
	class StringTypesTest {

		@Test
		@Order(301)
		void testString1() throws Exception {
			convertAndAssert("string1");
		}

		@Test
		@Order(302)
		void testString2() throws Exception {
			convertAndAssert("string2");
		}

		@Test
		@Order(303)
		void testStringNested() throws Exception {
			convertAndAssert("string-nested");
		}

	}

	@Nested
	class BooleanTypesTest {

		@Test
		@Order(401)
		void testBoolean1() throws Exception {
			convertAndAssert("boolean1");
		}

		@Test
		@Order(402)
		void testBoolean() throws Exception {
			convertAndAssert("boolean2");
		}

		@Test
		@Order(403)
		void testBooleanNested() throws Exception {
			convertAndAssert("boolean-nested");
		}

	}

	@Nested
	class ArrayTypesTest {

		@Test
		@Order(501)
		void testArray1() throws Exception {
			convertAndAssert("array1");
		}

		@Test
		@Order(502)
		void testArray2() throws Exception {
			convertAndAssert("array2");
		}

		@Test
		@Order(503)
		void testArray3() throws Exception {
			convertAndAssert("array3");
		}

		@Test
		@Order(504)
		void testArray4() throws Exception {
			convertAndAssert("array4");
		}

		@Test
		@Order(520)
		void testArraySingle() throws Exception {
			convertAndAssert("array-single");
		}

		@Test
		@Order(530)
		void testArrayNested() throws Exception {
			convertAndAssert("array-nested");
		}

	}

	@Nested
	class ObjectTypesTest {

		@Test
		@Order(601)
		void testObject() throws Exception {
			convertAndAssert("object");
		}

	}

	private void convertAndAssertThrows(String[] args) {
		assertThrows(IllegalArgumentException.class,
			() -> JsonToXmlConversionApp.main(args));
	}

	private void convertAndAssert(String fileName) {
		convertAndAssert(fileName, fileName);
	}

	private void convertAndAssert(String jsonFileName,
		String xmlFileName) {
		convertAndAssert(
			getArgs(jsonFileName, xmlFileName),
			xmlFileName);
	}

	private void convertAndAssert(String[] args, String xmlFileName) {
		// call to converter
		JsonToXmlConversionApp.main(args);
		// compare results
		compare(xmlFileName);
	}

	private void compare(String xmlFileName) {
		Diff diff = DiffBuilder
			.compare(Input.fromFile(getXmlFile(xmlFileName)))
			.withTest(Input.fromFile(getSampleXmlFile(xmlFileName)))
			.build();
		if (diff.hasDifferences()) {
			log.error(diff.toString(new DefaultComparisonFormatter()));
		}
		assertFalse(diff.hasDifferences());
	}

	private File getJsonFile(String fileName) {
		return getFile(getInputJsonFileName(fileName));
	}

	private File getXmlFile(String fileName) {
		return getFile(getOutputXmlFileName(fileName));
	}

	private File getSampleXmlFile(String fileName) {
		return getFile(getSampleXmlFileName(fileName));
	}

	private File getFile(String fileName) {
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

	private String[] getArgs(String jsonFileName,
		String xmlFileName) {
		return new String[] {
			getJsonFilePath(jsonFileName),
			getXmlFilePath(xmlFileName)
		};
	}

	private String getJsonFilePath(String fileName) {
		return getJsonFile(fileName).getAbsolutePath();
	}

	private String getXmlFilePath(String fileName) {
		return getXmlFile(fileName).getAbsolutePath();
	}

	private String getXmlFileDir(String fileName) {
		return getXmlFile(fileName).getParent();
	}

}
