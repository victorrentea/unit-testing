package victor.testing.tools;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import lombok.SneakyThrows;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.InputStream;

public class Canonical {
  private static final Configuration CONFIG = Configuration.builder()
      .jsonProvider(new JacksonJsonNodeJsonProvider())
      .mappingProvider(new JacksonMappingProvider())
      .build();

  @SneakyThrows
  public static DocumentContext load(String name) {
    String fileNameInClasspath = "/canonical/" + name;
    InputStream stream = Canonical.class.getResourceAsStream(fileNameInClasspath);
    if (stream == null) {
      throw new IllegalArgumentException("File not found: classpath:/" + fileNameInClasspath);
    }
    String originalJson = IOUtils.toString(stream);
    return JsonPath.using(CONFIG)
        .parse(originalJson);
  }
}
