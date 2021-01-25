package csw.assignment.json.xml.converter.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import csw.assignment.json.xml.converter.exception.ConversionRuntimeException;
import csw.assignment.json.xml.converter.jackson.MapperUtils;
import lombok.extern.log4j.Log4j2;


/**
 * The Default implementation of {@link JsonXmlConverter} for JSON to XML
 * Converter.
 *
 * @author praveen_kumar_nr
 */
@Log4j2
public class DefaultJsonXmlConverter implements JsonXmlConverter {

	/** The Constant TRANSFORMER. */
	private static final Transformer TRANSFORMER = createTransformer();

	/** The Constant UTF_8. */
	private static final String UTF_8 = "UTF-8";

	/** The Constant INDENT_AMOUNT. */
	private static final String INDENT_AMOUNT = "2";

	/** The mapper utils. */
	private final MapperUtils mapperUtils = MapperUtils.getInstance();

	/**
	 * Creates the transformer.
	 *
	 * @return the transformer
	 */
	private static Transformer createTransformer() {
		try {
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, UTF_8);
			transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", INDENT_AMOUNT);
			return transformer;
		} catch (TransformerConfigurationException exception) {
			log.error("Unable to Create Transformer, reason : {}",
				exception.getMessage());
			throw new ConversionRuntimeException(exception);
		}
	}

	@Override
	public boolean convertJsontoXml(File jsonFile, File xmlFile) {
		Optional<String> xmlOpt = mapperUtils.toXml(jsonFile);
		if (xmlOpt.isEmpty()) {
			throw new ConversionRuntimeException(
				"Generated an empty XML content for" + jsonFile.toString());
		}
		xmlOpt.ifPresent(xml -> {
			format(xml, xmlFile);
		});
		return true;
	}

	/**
	 * Format.
	 *
	 * @param xml     the xml
	 * @param xmlFile the xml file
	 */
	private void format(String xml, File xmlFile) {
		try (OutputStreamWriter writer = new OutputStreamWriter(
			new FileOutputStream(xmlFile), UTF_8)) {
			Source xmlInput = new StreamSource(new StringReader(xml));
			TRANSFORMER.transform(xmlInput, new StreamResult(writer));
		} catch (Exception exception) {
			log.error("Unable to Format and write XML content {}"
				+ " into {}, reason : {}",
				xml, xmlFile, exception.getMessage());
			throw new ConversionRuntimeException(exception);
		}
	}

	/**
	 * Format.
	 *
	 * @param xml the xml
	 * @return the string
	 */
	private String format(String xml) {
		try {
			Source xmlInput = new StreamSource(new StringReader(xml));
			StringWriter stringWriter = new StringWriter();
			StreamResult xmlOutput = new StreamResult(stringWriter);
			TRANSFORMER.transform(xmlInput, xmlOutput);
			return xmlOutput.getWriter().toString();
		} catch (Exception exception) {
			log.error("Unable to Format XML content {}, reason : {}",
				xml, exception.getMessage());
			throw new ConversionRuntimeException(exception);
		}
	}

	/**
	 * Format and write.
	 *
	 * @param xml     the xml
	 * @param xmlFile the xml file
	 */
	private void formatAndWrite(String xml, File xmlFile) {
		try (FileWriter fileWriter = new FileWriter(xmlFile)) {
			String formattedXml = format(xml);
			fileWriter.write(formattedXml);
		} catch (IOException exception) {
			log.error("Unable to wite XML content {} into {}, reason : {}",
				xml, xmlFile, exception.getMessage());
			throw new ConversionRuntimeException(exception);
		}
	}

	/**
	 * Format with LSS.
	 *
	 * @param xml the xml
	 * @return the string
	 */
	private String formatWithLSS(String xml) {
		try {
			final InputSource src = new InputSource(
				new StringReader(StringUtils.strip(xml)));
			final Node document = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().parse(src)
				.getDocumentElement();

			final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
			final DOMImplementationLS impl = (DOMImplementationLS)registry
				.getDOMImplementation("LS");
			final LSSerializer writer = impl.createLSSerializer();

			DOMConfiguration domConfig = writer.getDomConfig();
			// Set this to true if the output needs to be beautified.
			domConfig.setParameter("format-pretty-print", Boolean.TRUE);
			// Set this to true if the declaration is needed to be generated.
			domConfig.setParameter("xml-declaration", Boolean.FALSE);

			return writer.writeToString(document);
		} catch (Exception exception) {
			log.error("Unable to Format XML content {}, reason : {}",
				xml, exception.getMessage());
			throw new ConversionRuntimeException(exception);
		}
	}

}
