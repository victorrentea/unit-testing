package victor.testing.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.PolyRaw;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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

    public void createProduct(ProductDto productDto) {
        boolean safe = safetyClient.isSafe(productDto.barcode);
        if (!safe) {
            throw new IllegalStateException("Product is not safe: " + productDto.barcode);
        }

        Product product = new Product();
        product.setName(productDto.name);
        product.setCategory(productDto.category);
        product.setBarcode(productDto.barcode);
        product.setSupplier(supplierRepo.findById(productDto.supplierId).orElseThrow());
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

//    @Transactional(propagation = Propagation.REQUIRES_NEW) // oups: buseste testul
    public void horror() {
        productRepo.save(new Product());
    }
    // proxy-ul din fata comit not matter what. Tx original din teste  (urma sa faca ROLLBACK)
    // care a venit in fct asta NU POATE intra. Se face o noua Tx > COMMIT la ea.
}

