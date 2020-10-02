package victor.testing.spring.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import victor.testing.spring.facade.ProductFacade;
import victor.testing.spring.facade.ProductSearchResult;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
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

    @Test
    public void testSearch() throws Exception {
        when(facade.searchProduct(any())).thenReturn(Arrays.asList(new ProductSearchResult(1L, "Tree")));
        mockMvc.perform(post("/product/search")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(header().string("Custom-Header", "true"))
//                .andExpect(content().string(contains("Tree")))
                .andExpect(jsonPath("$[0].name").value("Tree"));
    }


}
