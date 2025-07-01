package victor.testing.spring;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class SafetyApiWireMock {

  public static void stubResponse(String barcode, String responseSafetyCategory) {
    stubFor(get(urlEqualTo("/product/" + barcode + "/safety"))
        .willReturn(okJson("""
            {
              "category": "%s",
              "detailsUrl": "http://details.url/a/b"
            }
            """.formatted(responseSafetyCategory))));
  }
}
