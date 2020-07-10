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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductFacade;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;
import ro.victor.unittest.spring.facade.ProductSearchResult;
import ro.victor.unittest.spring.repo.ProductRepo;
import ro.victor.unittest.spring.repo.RealDBRepoTest;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc // deeper e2e tests
@RealDBRepoTest
public class ProductControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepo repo;

//    @MockBean
//    private ProductFacade facade;


    @Autowired
    private ProductController controller;

    @Test
    public void testSearch() throws Exception {

        Stream.of("Haine", "Mancare", "Apa", "Bautura", "Masti")
            .map(Product::new)
            .forEach(repo::save);
//        ProductSearchCriteria criteria;
//        controller.search(criteria);

//        ProductSearchCriteria criteria = new ProductSearchCriteria();
//        String json = new ObjectMapper().writeValueAsString(criteria);

//        when(facade.searchProduct(any())).thenReturn(Arrays.asList(new ProductSearchResult(1L, "Tree")));
        MockHttpServletRequestBuilder request = post("/product/search")
            .content("{\"name\":\"c\"}") // ASTA
            .contentType(MediaType.APPLICATION_JSON); //
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(header().string("Custom-Header", "true")) // ASTA
//                .andExpect(content().string(contains("Tree")))
                .andExpect(jsonPath("$[0].name").value("Mancare"));
    }





}
