package victor.testing.spring.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import victor.testing.spring.api.dto.ProductDto;
import victor.testing.spring.api.dto.ProductSearchCriteria;
import victor.testing.spring.api.dto.ProductSearchResult;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// test-helper class
@Component
@ConditionalOnBean(MockMvc.class)
public class ProductApiTestDSL {
  private final static ObjectMapper jackson = new ObjectMapper().registerModule(new JavaTimeModule());

  @Autowired
  MockMvc mockMvc;

  public void createProductApi(ProductDto request) throws Exception {
    mockMvc.perform(createProductRequest(request))
        .andExpect(status().is2xxSuccessful())
        .andExpect(header().exists("Location"));
  }

  public MockHttpServletRequestBuilder createProductRequest(ProductDto request) throws JsonProcessingException {
    return post("/product/create")
        .content(jackson.writeValueAsString(request))
        .contentType(APPLICATION_JSON);
  }

  public List<ProductSearchResult> searchProductApi(ProductSearchCriteria criteria) throws Exception {
    String responseJson = mockMvc.perform(post("/product/search")
            .content(jackson.writeValueAsString(criteria))
            .contentType(APPLICATION_JSON)
        )
        .andExpect(status().is2xxSuccessful())
        .andReturn()
        .getResponse()
        .getContentAsString();
    return List.of(jackson.readValue(responseJson, ProductSearchResult[].class)); // trick to unmarshall a collection<obj>
  }

  public ProductDto getProductApi(long productId) throws Exception {
    String responseJson = mockMvc.perform(get("/product/{id}", productId))
        .andExpect(status().is2xxSuccessful())
        .andReturn().getResponse().getContentAsString();
    return jackson.readValue(responseJson, ProductDto.class);
  }
}
