package ro.victor.unittest.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductFacade;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;
import ro.victor.unittest.spring.facade.ProductSearchResult;
import ro.victor.unittest.spring.repo.ProductRepo;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest // Start up only web part. Requires @MockBean for deeper layers
//@WebMvcTest(value=ProductController.class) // - Even more fine-grained
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"db-mem", "test"})
@Transactional
public class ProductControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

//    @MockBean
//    private ProductFacade facade;

    @Autowired
    private ProductRepo productRepo;
    @Test
//    @WithMockUser(role="ADMIN")
    public void testSearch() throws Exception {
//        productRepo.save(new Product().setName("Tree"));

        mockMvc.perform(post("/product")
            .content("{\"productName\": \"Tree\"}")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());



        ProductSearchCriteria criteria = new ProductSearchCriteria();
        criteria.name = "Re";
        String criteriaJson = new ObjectMapper().writeValueAsString(criteria);
//        when(facade.searchProduct(any())).thenReturn(Arrays.asList(new ProductSearchResult(1L, "Tree")));
        mockMvc.perform(post("/product/search")
                .content(criteriaJson)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(header().string("Custom-Header", "true"))
//                .andExpect(content().string(contains("Tree")))
                .andExpect(jsonPath("$[0].name").value("Tree"));
    }


}
