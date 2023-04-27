package victor.testing.spring.web;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import victor.testing.spring.service.ProductService;
import victor.testing.spring.web.dto.ProductDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static victor.testing.spring.domain.ProductCategory.HOME;

@WebMvcTest // slice tests
public class ControllerTest {
  @Autowired
  MockMvc mockMvc;
  @MockBean
  ProductService productService;
  @Test
  @WithMockUser(roles = "ADMIN")
  void createProduct() throws Exception {
    // language=json
    String createJson = ("""
                {
                  "name": "Copac",
                  "supplierId": "13",
                  "category": "HOME",
                  "barcode": "safebar"
                }
                """);

    mockMvc.perform(post("/product/create")
                    .content(createJson)
                    .contentType(APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
    ;

    Mockito.verify(productService).createProduct(new ProductDto()
            .setName("Copac")
            .setSupplierId(13L)
            .setCategory(HOME)
            .setBarcode("safebar"));
  }
}
