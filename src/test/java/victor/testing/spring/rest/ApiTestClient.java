package victor.testing.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.spring.rest.dto.ProductSearchCriteria;
import victor.testing.spring.rest.dto.ProductSearchResult;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Component
public class ApiTestClient {
  private final static ObjectMapper jackson = new ObjectMapper().registerModule(new JavaTimeModule());

  @Autowired(required = false)
  MockMvc mockMvc;

  @SneakyThrows
  public long createProduct(ProductDto request) {
    MockHttpServletResponse response = mockMvc.perform(createProductRequest(request))
        .andExpect(status().is2xxSuccessful())
        .andExpect(header().exists("Location"))
        .andReturn()
        .getResponse();
    String url = response.getHeader("Location");// e.g. /product/123
    return Long.parseLong(url.substring(url.lastIndexOf('/') + 1));
  }

  @SneakyThrows
  public MockHttpServletRequestBuilder createProductRequest(ProductDto request) {
    return post("/product/create")
        .content(jackson.writeValueAsString(request))
        .contentType(APPLICATION_JSON);
  }

  @SneakyThrows
  public List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) {
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

  @SneakyThrows
  public ProductDto getProduct(long productId) {
    String responseJson = mockMvc.perform(get("/product/{id}", productId))
        .andExpect(status().is2xxSuccessful())
        .andReturn().getResponse().getContentAsString();
    return jackson.readValue(responseJson, ProductDto.class);
  }

  @SneakyThrows
  public void deleteProduct(Long productId) {
    mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id}", productId))
        .andExpect(status().is2xxSuccessful());
  }
}
