package play;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

@Slf4j
public class MaskingUtils {
  private static boolean piiMaskingEnabled;

  public static String maskPIIDataFromXml(String xml) {
        if (!isPiiMaskingEnabled()) {
            return xml; // Return XML as it is if PII masking is not enabled
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));

            maskPIIElements(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));

            return writer.toString();
        } catch (Exception e) {
            log.error("Error processing XML for masking PII data: {}", e);
            throw new RuntimeException("Error processing XML for masking PII data", e);
        }
    }

  private static void maskPIIElements(Document document) {
        try {
          if (!isPiiMaskingEnabled()) {
                return; // Do not mask if PII masking is not enabled
            }

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            List<String> piiMaskingFields = getPiiMaskingFields();
            List<String> emailMaskingFields = getEmailMaskingFields();

            for (String key : piiMaskingFields) {
                XPathExpression expression = xpath.compile("//" + key);
                NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String nodeValue = element.getTextContent();
                        int maskLength = getPiiVisibleCharacters();
                        element.setTextContent(maskValue(nodeValue, maskLength));
                    }
                }
            }

            for (String key : emailMaskingFields) {
                XPathExpression expression = xpath.compile("//" + key);
                NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String nodeValue = element.getTextContent();
                        element.setTextContent(maskEmailValue(nodeValue));
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error masking PII elements in XML: {}", e);
            throw new RuntimeException("Error masking PII elements in XML", e);
        }
    }

  private static List<String> getEmailMaskingFields() {
    return List.of("email");
  }

  private static List<String> getPiiMaskingFields() {
    return List.of("ssn", "creditCardNumber", "passportNumber");
  }

  private static String maskEmailValue(String nodeValue) {
    return nodeValue.replaceAll("(^[^@]{3}|(?!^)\\G)[^@]", "$1*");
  }

  private static String maskValue(String nodeValue, int maskLength) {
    return nodeValue.substring(0, maskLength) + nodeValue.substring(maskLength).replaceAll(".", "*");
  }

  private static int getPiiVisibleCharacters() {
    return 0;
  }

  public static boolean isPiiMaskingEnabled() {
    return piiMaskingEnabled;
  }

  public static void setPiiMaskingEnabled(boolean piiMaskingEnabled) {
    MaskingUtils.piiMaskingEnabled = piiMaskingEnabled;
  }
}

