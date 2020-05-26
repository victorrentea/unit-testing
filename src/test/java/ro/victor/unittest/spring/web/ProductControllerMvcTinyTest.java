package ro.victor.unittest.spring.web;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
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
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.facade.ProductFacade;
import ro.victor.unittest.spring.facade.ProductSearchResult;
import ro.victor.unittest.spring.repo.ProductRepo;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerMvcTinyTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductFacade facade;

    @WithMockUser("spring")
    @Test
    public void search() throws Exception {
        when(facade.searchProduct(ArgumentMatchers.anyObject()))
                .thenReturn(Arrays.asList(new ProductSearchResult(13L, "Cuie")));
        mockMvc.perform(post("/product/search").content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Matchers.equalTo("Cuie")));
//                .andExpect(content().string("[]"));
    }
    @Test
    public void securedIsDeniedForAnonymous() throws Exception {
        mockMvc.perform(get("/secured"))
                .andExpect(status().isUnauthorized());
    }
    @WithMockUser("spring")
    @Test
    public void securedIsAccessible() throws Exception {
        mockMvc.perform(get("/secured"))
                .andExpect(status().isOk());
    }
    @Test
    public void unsecuredIsAccessibleForAnnonymous() throws Exception {
        mockMvc.perform(get("/unsecured"))
                .andExpect(status().isOk());
    }
    @WithMockUser(roles = "ADMIN")
    @Test
    public void criticalAccessibleByAdmin() throws Exception {
        mockMvc.perform(get("/critical"))
                .andExpect(status().isOk());
    }
}
