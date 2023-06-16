package victor.testing.spring.product.api;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import victor.testing.spring.BaseIntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;


@WebMvcTest
public class ProductApiAuthorizationTest extends BaseIntegrationTest {
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
