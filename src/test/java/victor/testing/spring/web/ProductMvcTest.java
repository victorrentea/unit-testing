package victor.testing.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
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

    @Autowired
    private SupplierRepo supplierRepo;


    @MockBean
    private SafetyClient safetyClient;
    @Test
    public void testSearch() throws Exception {

        Mockito.when(safetyClient.isSafe("barcode")).thenReturn(true);
        Long supplierId = supplierRepo.save(new Supplier()).getId();

        ObjectMapper mapper = new ObjectMapper();
        ProductDto dto = new ProductDto("Tree", "barcode", supplierId, ProductCategory.WIFE);
        String createJson = mapper.writeValueAsString(dto);


        mockMvc.perform(post("/product/create")
            .content(createJson)
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk());


        mockMvc.perform(post("/product/search")
            .content("{}")
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(header().string("Custom-Header", "true"))
//            .andExpect(jsonPath("$", hasSize(1)));
            .andExpect(jsonPath("$[0].name").value("Tree"));
    }

    // The MockMvc EMULATES a HTTP call, w/o Tomcat, w/o any HTTP Worker Thread Pool,
    // I'm running the COntroller in the same thread as the test
    // and since the Transaction in Spring is bound to the current thread ->
    // I can make sure the .search repo works in the same Tx as the test one,
    // in which I INSERTed the Product

    @Test
    public void testSearc2h() throws Exception {
        productRepo.save(new Product().setName("Tree"));

        mockMvc.perform(post("/product/search")
            .content("{}")
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(header().string("Custom-Header", "true"))


            .andExpect(jsonPath("$[0].name").value("Tree"));
    }


}
