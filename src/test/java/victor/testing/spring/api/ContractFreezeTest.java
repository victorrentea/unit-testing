package victor.testing.spring.api;

import org.junit.jupiter.api.Test;
import org.openapitools.openapidiff.core.OpenApiCompare;
import org.openapitools.openapidiff.core.model.ChangedOpenApi;
import org.openapitools.openapidiff.core.output.MarkdownRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@ActiveProfiles("db-mem")
@AutoConfigureMockMvc
public class ContractFreezeTest {
  @Autowired
  MockMvc mockMvc;

  @Value("classpath:/my-openapi.json")
  Resource myPreviousContractResource;

  @Test
  void my_contract_did_not_change() throws Exception {
    String previousOpenAPIJson = Files.readString(myPreviousContractResource.getFile().toPath());

    String currentOpenAPIJson = mockMvc.perform(get("/v3/api-docs"))
                    .andReturn().getResponse().getContentAsString();

    ChangedOpenApi diff = OpenApiCompare.fromContents(previousOpenAPIJson, currentOpenAPIJson);

    if (!diff.isCompatible()) {
      String render = new MarkdownRender().render(diff);
      System.err.println(render);

      assertThat(currentOpenAPIJson)
          .describedAs("Exposed OpenAPI should not have changed")
          .isEqualTo(previousOpenAPIJson);
    }
  }
}
