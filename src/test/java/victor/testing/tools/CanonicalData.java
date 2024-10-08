package victor.testing.tools;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;

public class CanonicalData {
  private static final Configuration CONFIG = Configuration.builder()
      .jsonProvider(new JacksonJsonNodeJsonProvider())
      .mappingProvider(new JacksonMappingProvider())
      .build();

  @SneakyThrows
  public static DocumentContext load(String name) {
    String fileNameInClasspath = "/canonical/" + name + ".json";
    InputStream stream = CanonicalData.class.getResourceAsStream(fileNameInClasspath);
    if (stream == null) {
      throw new IllegalArgumentException("File not found: classpath:/" + fileNameInClasspath);
    }
    String originalJson = IOUtils.toString(stream);
    return JsonPath.using(CONFIG)
        .parse(originalJson);
  }
}
