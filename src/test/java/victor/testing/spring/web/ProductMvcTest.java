package victor.testing.spring.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("db-mem")
public class ProductMvcTest {
    @Autowired
    private MockMvc mockMvc; // emulator de http requesturi
    // In acest fisier nu e nici o pisica (Tomcat)
    // ASADAR, testele ruleaza pe un singur thread de sus pana jos

    //Daca pui @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
    // se porneste tomcat, ii trimiti pe localhost:xxxx requestul tau
    // el delega requestul tau HTTP la un thread in worked pool-ul lui,
    // si NU mai ai cum sa opresti tranzactia de pe acel thread sa comita !

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductController controller;

    @Test
    public void testSearch() throws Exception {
        productRepo.save(new Product().setName("Tree"));

        ProductSearchCriteria criteria = new ProductSearchCriteria();
        criteria.name="rE";
        String criteriaJson = new ObjectMapper().writeValueAsString(criteria);


//        controller.search()
        // vs
        //: trece si prin filtre, prin tot ce porneste springul
        // acoperi: pathurile, status codes, JSON intors, content types, (headere) si @RestControllerAdvice
        // chestii care nu reprez biz logic, si 70% din ele sunt identice pt toare request
        mockMvc.perform(post("/product/search") // NU face socket connect la nimeni
            .content(criteriaJson)
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk()) //200 OK
            .andExpect(header().string("Custom-Header", "true"))   // headere
            .andExpect(jsonPath("$", hasSize(1))); // ca jsonul intors ....

//            .andExpect(jsonPath("$[0].name").value("Tree"));
    }



}
