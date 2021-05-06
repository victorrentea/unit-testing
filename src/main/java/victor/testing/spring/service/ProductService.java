package victor.testing.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.testing.spring.domain.Product;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ProductService {
    private final SafetyClient safetyClient;
    private final ProductRepo productRepo;
    private final SupplierRepo supplierRepo;

    public ProductService(SafetyClient safetyClient, ProductRepo productRepo, SupplierRepo supplierRepo) {
        this.safetyClient = safetyClient;
        this.productRepo = productRepo;
        this.supplierRepo = supplierRepo;
    }

//    @PreAuthorize("hasRole('ADMIN')")
    public void createProduct(ProductDto productDto) {
        boolean safe = safetyClient.isSafe(productDto.barcode);
        if (!safe) {
            throw new IllegalStateException("Product is not safe: " + productDto.barcode);
        }

        Product product = new Product();
        product.setName(productDto.name);
        product.setCategory(productDto.category);
        product.setBarcode(productDto.barcode);
        product.setSupplier(supplierRepo.getOne(productDto.supplierId));
        // TODO CR check that the supplier is active!
        product.setCreateDate(LocalDateTime.now());
        productRepo.save(product);
    }

    public List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) {
        return productRepo.search(criteria);
    }

    public boolean isActive(long productId) {
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        return productRepo.findById(productId).get()
            .getCreateDate().isAfter(oneYearAgo);
    }
}
