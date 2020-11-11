package victor.testing.spring.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.tools.WireMockExtension;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest(properties = "safety.service.url.base=http://localhost:8989")
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

   // ce fel de buguri poti prinde daca pornesti si tomcat-ul din teste ?
   // HTTPS(ssl), max upload size, load test de worker thread exhaustion,
   // pe scurt toate proprietatile din application properties care contin "tomcat"

   @Autowired
   private ProductRepo productRepo; // date operationale.

   @Autowired
   private SupplierRepo supplierRepo; // reference data. nomenclatoare. dictionare. date statice.

   @Autowired
   private ProductController controller;

   @RegisterExtension
   public WireMockExtension wireMock = new WireMockExtension(8989);

   private Long supplierId;

   @BeforeEach
   public void initialize() {
      supplierId = supplierRepo.save(new Supplier()).getId();
   }


   // ca sa nu mai depind de Safety Service pot:
   // 1) @MockBean
   // 2) WireMock
   @Test
//   @WithMockUser(roles = "ADMIN")
   public void testSearch() throws Exception {
      createProduct(aProductDto());

      ProductSearchCriteria criteria = new ProductSearchCriteria().setName("rE");

      List<ProductSearchResult> results = searchProduct(criteria);
      assertOneResult(results, p -> p.getName().equals("Tree"));
   }


   // NU FA D;ASTEA DECAT PE APIURILE EXPUSE CATRE ALTE PROIECTE/ECHIPE. NU PE APIURILE CATRE PROPRIA SPA./UI
   @Test
   public void testSearch2() throws Exception {
      createProduct(aProductDto());

      ProductSearchCriteria criteria = new ProductSearchCriteria().setName("Tree");

      List<ProductSearchResult> results = searchProduct(criteria);
      assertOneResult(results, p -> p.getName().equals("Tree"));
   }
   @Test
   public void testSearch2GlobaExHand() throws Exception {
      ProductSearchCriteria criteria = new ProductSearchCriteria().setName("A");
      String criteriaJson = new ObjectMapper().writeValueAsString(criteria);
      ResultActions asta = mockMvc.perform(post("/product/search") // NU face socket connect la nimeni
          .content(criteriaJson)
          .contentType(MediaType.APPLICATION_JSON)
      );
      asta
          .andExpect(status().isOk());
   }

   private void assertOneResult(List<ProductSearchResult> results, Predicate<? super ProductSearchResult> predicate) {
      assertThat(results).hasSize(1);
      assertThat(results).allMatch(predicate);
   }

   private ProductDto aProductDto() {
      return new ProductDto("Tree", "1", this.supplierId, ProductCategory.HOME);
   }

   private List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) throws Exception {
      //        controller.search()
      // vs
      //: trece si prin filtre, prin tot ce porneste springul
      // acoperi: pathurile, status codes, JSON intors, content types, (headere) si @RestControllerAdvice
      // chestii care nu reprez biz logic, si 70% din ele sunt identice pt toare request
      String criteriaJson = new ObjectMapper().writeValueAsString(criteria);
      ResultActions asta = mockMvc.perform(post("/product/search") // NU face socket connect la nimeni
          .content(criteriaJson)
          .contentType(MediaType.APPLICATION_JSON)
      );
      asta
          .andExpect(status().isOk()) //200 OK
          .andExpect(header().string("Custom-Header", "true"))   // headere
          .andExpect(jsonPath("$", hasSize(1))); // ca jsonul intors ....
      String responseJson = asta.andReturn().getResponse().getContentAsString();
//        new ObjectMapper().readValue(responseJson, List<ProductSearchResult>.class); // nu merge ca <> se sterge la javac
      return new ObjectMapper().readValue(responseJson, new TypeReference<List<ProductSearchResult>>() {
      });
   }

   private void createProduct(ProductDto productDto) throws Exception {
      String productJson = new ObjectMapper().writeValueAsString(productDto);
      mockMvc.perform(post("/product/create") // NU face socket connect la nimeni
          .content(productJson)
          .contentType(MediaType.APPLICATION_JSON)
      )
          .andExpect(status().isOk()) //200 OK
          .andExpect(header().string("Custom-Header", "true"));
   }


}
