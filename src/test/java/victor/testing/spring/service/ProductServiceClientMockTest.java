package victor.testing.spring.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("db-mem")
public class ProductServiceClientMockTest {
	@MockBean
	public SafetyClient mockSafetyClient;
	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private SupplierRepo supplierRepo;
	@Autowired
	private ProductService productService;

	@Test
	public void throwsForUnsafeProduct() {
		Assertions.assertThrows(IllegalStateException.class, () -> {
			when(mockSafetyClient.isSafe("upc")).thenReturn(false);
			ProductDto productDto = new ProductDto();
			productDto.setUpc("upc");
			productService.createProduct(productDto);
		});
	}

	@Test
	public void fullOk() {
		Long supplierId = supplierRepo.save(new Supplier()).getId();
		when(mockSafetyClient.isSafe("upc")).thenReturn(true);

		ProductDto productDto = new ProductDto("name", "upc", supplierId, ProductCategory.HOME);
		long productId = productService.createProduct(productDto);

		Product product = productRepo.findById(productId).get();

		assertThat(product.getName()).isEqualTo("name");
		assertThat(product.getUpc()).isEqualTo("upc");
		assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
		assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
		assertThat(product.getCreateDate()).isNotNull();
	}

	// TODO Fixed Time
	// @TestConfiguration public static class ClockConfig { @Bean @Primary public
	// Clock fixedClock() {}}

	// TODO Variable Time
	// when(clock.instant()).thenAnswer(call ->
	// currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
	// when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}
