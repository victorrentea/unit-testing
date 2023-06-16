package victor.testing.spring.product.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.BaseDatabaseTest;
import victor.testing.spring.product.api.dto.ProductDto;
import victor.testing.spring.product.api.dto.ProductSearchCriteria;
import victor.testing.spring.product.api.dto.ProductSearchResult;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.repo.ProductRepo;
import victor.testing.spring.product.repo.SupplierRepo;
import victor.testing.tools.HumanReadableTestNames;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static victor.testing.spring.product.domain.ProductCategory.HOME;


@WebMvcTest
public class ProductApiAuthorizationTest extends BaseDatabaseTest {
  @Autowired
  MockMvc mockMvc;

  @ParameterizedTest
  @CsvSource({
      "POST,/product/create,USER,false",
      "POST,/product/create,ADMIN,false",
  })
  void createProductByNonAdmin_NotAuthorized(HttpMethod method, String url,
                                             String role, String accessAllowed) throws Exception {

//    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("user", "password", List.of("ROLE_" + role));
//    SecurityContext context = SecurityContextHolder.createEmptyContext();
//    context.setAuthentication(token);
//    SecurityContextHolder.setContext(context);
//    mockMvc.perform(request(method,url))
//        .andExpect( accessAllowed ?stat :  status().isForbidden());
  }


}
