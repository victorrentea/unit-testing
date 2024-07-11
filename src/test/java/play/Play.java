package play;

import lombok.extern.slf4j.Slf4j;

import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.Map;

@Slf4j
public class Play {

  public String getContent(StringBuilder xmlString, String stylesheet, Map dic) {
    try {
      String maskedXmlString = MaskingUtils.maskPIIDataFromXml(xmlString.toString());
      XSLTService xsltService = (XSLTService) TurbineServices.getInstance().getService(XSLTService.SERVICE_NAME);
      return xsltService.transform(
          new StreamSource(new StringReader(maskedXmlString)),
          PortalResources.getResource(stylesheet),
          dic);
    } catch (Exception e) {
      log.error("Exception Occured in content transform ", e);
      return "";
    }
  }
}
