package victor.testing.spring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import lombok.SneakyThrows;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.infra.SafetyEntryDto;
import victor.testing.spring.infra.SafetyReportDto;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.tools.WireMockExtension;
import victor.testing.spring.web.dto.ProductDto;
import wiremock.org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "safety.service.url.base=http://localhost:8089")
@ActiveProfiles("db-mem")
public class ProductServiceClientWireMockTest {
	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private SupplierRepo supplierRepo;
	@Autowired
	private ProductService productService;

	@Rule
	public WireMockRule wireMock = new WireMockRule(8089);

	@Test(expected = IllegalStateException.class)
	public void throwsForUnsafeProduct() {
		productService.createProduct(new ProductDto("name", "2", -1L, ProductCategory.HOME));
	}

	@Test(expected = IllegalStateException.class)
	public void throwsForUnsafeProductProgrammaticWireMock() throws JsonProcessingException {
		SafetyReportDto safetyReportDto = new SafetyReportDto();
		safetyReportDto.setEntries(new ArrayList<>());
		SafetyEntryDto entry = new SafetyEntryDto();
		entry.setCategory("DETERMINED");
		entry.setDetailsUrl("http://bla");
		safetyReportDto.getEntries().add(entry);
		
		String json = new ObjectMapper().writeValueAsString(safetyReportDto);
		
		WireMock.stubFor(get(urlEqualTo("/product/customXX/safety"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(json))); // override
		
		productService.createProduct(new ProductDto("name", "customXX", -1L, ProductCategory.HOME));
	}
//
//	@Test(expected = IllegalStateException.class)
//	public void throwsForUnsafeProductProgrammaticWireMockFromFileTemplatized()
//			throws FileNotFoundException, IOException {
//		String template;
//		try (FileReader reader = new FileReader(
//				"C:\\workspace\\integration-testing-spring\\src\\test\\java\\victor\\testing\\spring\\facade\\inTemplate.json")) {
//			template = IOUtils.toString(reader);
//		}
//		template.replace("{{}}", "DYNAMIC STUFF");
//		WireMock.stubFor(get(urlEqualTo("/product/customXX/safety"))
//				.willReturn(aResponse()
//						.withStatus(200)
//						.withHeader("Content-Type", "application/json")
//						.withBody(template))); // override
//
//		productService.createProduct(new ProductDto("name", "customXX", -1L, ProductCategory.HOME));
//	}

	@Test
	public void fullOk() {
		long supplierId = supplierRepo.save(new Supplier()).getId();

		ProductDto dto = new ProductDto("name", "1", supplierId, ProductCategory.HOME);
		productService.createProduct(dto);

		Product product = productRepo.findAll().get(0);
//		LocalDateTime today = LocalDateTime.parse("2014-12-22T10:15:30.00");

		assertThat(product.getName()).isEqualTo("name");
		assertThat(product.getUpc()).isEqualTo("1");
		assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
		assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
//		assertThat(product.getCreateDate()).isEqualTo(today);
	}

	@TestConfiguration
	public static class TestConfig {
		@Bean
		@Primary
		public Clock clockFixed() {
			return Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"), ZoneId.systemDefault());
		}
	}

	// TODO Fixed Time
	// @TestConfiguration public static class ClockConfig { @Bean @Primary public
	// Clock fixedClock() {}}

	// TODO Variable Time
	// when(clock.instant()).thenAnswer(call ->
	// currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
	// when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}
