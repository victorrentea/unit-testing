package victor.testing.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.web.dto.ProductDto;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("db-mem")
public class ProductMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepo productRepo;

    @Test
    public void testSearch() throws Exception {
        productRepo.save(new Product().setName("Tree"));

        mockMvc.perform(post("/product/search")
            .content("{}")
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(header().string("Custom-Header", "true"))
            .andExpect(jsonPath("$", hasSize(1)));
//            .andExpect(jsonPath("$[0].name").value("Tree"));
    }

    // The MockMvc EMULATES a HTTP call, w/o Tomcat, w/o any HTTP Worker Thread Pool,
    // I'm running the COntroller in the same thread as the test
    // and since the Transaction in Spring is bound to the current thread ->
    // I can make sure the .search repo works in the same Tx as the test one,
    // in which I INSERTed the Product


}
