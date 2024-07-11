package play;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import javax.xml.transform.stream.StreamSource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayTest {

  @Mock
  XSLTService mockXsltService;

  @BeforeEach
  final void setup() {
    TurbineServices.getInstance().putMockService(XSLTService.SERVICE_NAME, mockXsltService);
  }

  @AfterEach
  final void tearDown() {
    TurbineServices.getInstance().clearMockServices();
  }

  @Test
  void getContent() throws IOException {
    when(mockXsltService.transform(any(), any(), any())).thenReturn("Hello");

    // when
    String result = new Play().getContent(new StringBuilder("<phone>123</phone><email>abc</email><card>1234</card>"), "stylesheet", null);

    // then
    ArgumentCaptor<StreamSource> captor = ArgumentCaptor.forClass(StreamSource.class);
    verify(mockXsltService).transform(captor.capture(), any(), any());
    String maskedXmlString = IOUtils.toString(captor.getValue().getReader());

    assertThat(maskedXmlString).isEqualTo("<phone>***</phone><email>***</email><card>***</card>");

    assertThat(result).isEqualTo("Hello");
  }
  // better, point the REAL xlstService to a dummy stylesheet like by
  // in /src/test/resource have a dummy.xsl file
  //
  @Test
  void getContentSocialForReal() throws IOException {

    // when
    String result = new Play().getContent(new StringBuilder("<phone>123</phone><email>abc</email><card>1234</card>"), "dummy.xsl", null);

    // then
    ArgumentCaptor<StreamSource> captor = ArgumentCaptor.forClass(StreamSource.class);
    verify(mockXsltService).transform(captor.capture(), any(), any());
    String maskedXmlString = IOUtils.toString(captor.getValue().getReader());

    assertThat(maskedXmlString).isEqualTo("<phone>***</phone><email>***</email><card>***</card>");

    assertThat(result).isEqualTo("Hello");
  }



}