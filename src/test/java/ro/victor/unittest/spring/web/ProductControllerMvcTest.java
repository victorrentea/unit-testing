package ro.victor.unittest.spring.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductFacade;
import ro.victor.unittest.spring.facade.ProductSearchResult;
import ro.victor.unittest.spring.repo.ProductRepo;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("db-real")
@Transactional
@SpringBootTest @AutoConfigureMockMvc // deeper e2e tests
public class ProductControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepo productRepo;
//    @MockBean
//    private ProductFacade facade;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSearch() throws Exception {
//        ProductSearchResult result = new ProductSearchResult(1L, "Tree");
//        when(facade.searchProduct(any())).thenReturn(asList(result));


        // in loc de asta:
        productRepo.save(new Product("Tree"));

//        mockMvc.perform(post("/product") ) // cu un body care sa creeze produsul


        mockMvc.perform(post("/product/search")
                .content("{\"name\":\"re\"}")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(header().string("Custom-Header", "true"))
//                .andExpect(content().string(contains("Tree")))

                .andExpect(jsonPath("$[0].name").value("Tree"));
    }


}
