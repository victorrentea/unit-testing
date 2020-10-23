package victor.testing.spring.web;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(properties = "safety.service.url.base=http://localhost:8089")
@AutoConfigureMockMvc
@ActiveProfiles("db-mem")
@Transactional
public class ProductMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SupplierRepo supplierRepo;
    
    @Rule
    public WireMockRule wireMock = new WireMockRule(8089);

    @Test
    public void testSearch() throws Exception {
    	Long supplierId = supplierRepo.save(new Supplier()).getId();
//        productRepo.save(new Product().setName("Tree"));
        
    	ProductDto dto = new ProductDto();
    	dto.name="Tree";
    	dto.upc="1";
    	dto.supplierId = supplierId;
    	String createJson = new ObjectMapper().writeValueAsString(dto);
        

    	mockMvc.perform(post("/product/create")
    			.content(createJson)
    			.contentType(MediaType.APPLICATION_JSON)
    			)
	    	.andExpect(status().isOk())
	    	.andExpect(header().string("Custom-Header", "true"));


    	ProductSearchCriteria criteria = new ProductSearchCriteria();
    	
    	String searchJson = new ObjectMapper().writeValueAsString(criteria);
		String responseJson = mockMvc.perform(post("/product/search")
            .content(searchJson)
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(header().string("Custom-Header", "true"))
//            .andExpect(jsonPath("$", hasSize(1)))
            .andReturn().getResponse().getContentAsString();
		
		List<ProductSearchResult> results = new ObjectMapper().readValue(responseJson,
//				List<ProductSearchResult>.class
				new TypeReference<List<ProductSearchResult>>() {});
		
		
		assertThat(results).hasSize(1);
		
    }
}
