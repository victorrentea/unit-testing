package ro.victor.unittest.spring.web;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.facade.ProductFacade;
import ro.victor.unittest.spring.facade.ProductSearchResult;
import ro.victor.unittest.spring.repo.ProductRepo;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ProductControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepo productRepo;

    @Test
    public void unsecured() throws Exception {
        mockMvc.perform(get("/unsecured"))
                .andExpect(status().isOk())
                .andExpect(header().string("Head-Shot", "true"))
                .andExpect(content().string("1"));
    }

    @Test
    public void search() throws Exception {
        productRepo.save(new Product()
                .setName("Cuie")
                .setCategory(Product.Category.PT_NEVASTA)
                .setSupplier(new Supplier().setName("emag")));
        mockMvc.perform(post("/product/search").content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Matchers.equalTo("Cuie")));
//                .andExpect(content().string("[]"));
    }
}
