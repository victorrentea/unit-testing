package victor.testing.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import lombok.SneakyThrows;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WireMockMatching {

   WireMockServer wireMockServer;

   protected void stubExternalApiGet(String path, Map<String, String> queryParams, Object responseObject) {
      wireMockServer.stubFor(initGetApiWithParams(path, queryParams)
          .willReturn(aResponse().withBody(toJsonString(responseObject))));
   }

   @SneakyThrows
   private String toJsonString(Object responseObject) {
      return new ObjectMapper().writeValueAsString(responseObject);
   }

   private MappingBuilder initGetApiWithParams(String path, Map<String, String> queryParams) {
      MappingBuilder url = get(urlMatching(path + ".*"));

      for (Map.Entry<String, String> entry : queryParams.entrySet()) {
         url = url.withQueryParam(entry.getKey(), determineParameterCheck(entry.getValue()));
      }
      return url;
   }

   private StringValuePattern determineParameterCheck(String value) {
      if (value == null) {
         return absent();
      }
      if ("_ignore_".equals(value)) {
         return matching(".*");
      }
      if (value.startsWith("includes_")) {
         return matching(".*" + value.substring(9) + ".*");
      }
      return equalToIgnoreCase(value);
   }
}

