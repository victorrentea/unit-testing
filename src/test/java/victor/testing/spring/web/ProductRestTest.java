package victor.testing.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.tools.WireMockExtension;
import victor.testing.spring.web.dto.ProductDto;

import java.util.Random;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@Sql
@SpringBootTest(properties = "safety.service.url.base=http://localhost:9999")
@ActiveProfiles("db-mem")
@Transactional
@AutoConfigureMockMvc // emuleaza requesturi HTTP **FARA** sa porneasca Tomcat.
public class ProductRestTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SupplierRepo supplierRepo;

    @RegisterExtension
    public WireMockExtension wireMock = new WireMockExtension(9999);

    @Test
    public void testSearch() throws Exception {
        // date de referinta
        Long supplierId = supplierRepo.save(new Supplier()).getId();
//        productRepo.save(new Product().setName("Tree"));

        int r = new Random().nextInt(100);
        ProductDto dto = new ProductDto("Tree"+r, "SAFE", supplierId, ProductCategory.ME);
        String createJson = new ObjectMapper().writeValueAsString(dto);
        mockMvc.perform(post("/product/create")
            .content(createJson)
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        // TODO extract ID from create response

        mockMvc.perform(post("/product/search")
            .content("{\n" +
                     "  \"name\": \"E"+r+"\"\n" +
                     "}")
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(header().string("Custom-Header", "true"))
            .andExpect(jsonPath("$", hasSize(1)));
//            .andExpect(jsonPath("$[0].name").value("Tree"));

        // TODO
//        mockMvc.perform(delete("/product/"+idCreatMaiSus+"/delete")
//            .content()
//            .contentType(MediaType.APPLICATION_JSON)
//        )
    }
}
