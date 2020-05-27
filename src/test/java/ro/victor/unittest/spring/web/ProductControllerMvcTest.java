package ro.victor.unittest.spring.web;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.RequestBody;
import ro.victor.unittest.spring.facade.ProductFacade;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;
import ro.victor.unittest.spring.facade.ProductSearchResult;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest // Start up only web part. Requires @MockBean for deeper layers
//@WebMvcTest(value=ProductController.class) // - Even more fine-grained
//@SpringBootTest @AutoConfigureMockMvc // deeper e2e tests
public class ProductControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductFacade facade;

    // TODO read about Standalone setup:
    // https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/testing.html#spring-mvc-test-server-setup-options

    @WithMockUser("spring")
    @Test
    public void testSearch() throws Exception {
        when(facade.searchProduct(any())).thenReturn(Arrays.asList(new ProductSearchResult(1L, "dummyProduct1")));
        mockMvc.perform(post("/product/search")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(header().string("Head-Shot", "true"))
//                .andExpect(content().string(contains("dummyProduct1")))
                .andExpect(jsonPath("$[0].name").value("dummyProduct1"));
    }

    @Test
    public void testSearchUnauthorized() throws Exception {
        when(facade.searchProduct(any())).thenReturn(Arrays.asList(new ProductSearchResult(1L, "dummyProduct1")));
        mockMvc.perform(post("/product/search")).andExpect(status().isUnauthorized());
    }

    @Test
    public void testUnsecured() throws Exception {
        mockMvc.perform(get("/unsecured"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

}
