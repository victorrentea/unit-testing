package victor.testing.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class ProductService {
    private final SafetyClient safetyClient;
    private final ProductRepo productRepo;
    private final SupplierRepo supplierRepo;

    public long createProduct(ProductDto productDto) {
        boolean safe = safetyClient.isSafe(productDto.upc);
        if (!safe) {
            throw new IllegalStateException("Product is not safe: " + productDto.upc);
        }

        Product product = new Product();
        product.setName(productDto.name); // asta
        product.setCategory(productDto.category);
        product.setUpc(productDto.upc);
        product.setSupplier(supplierRepo.getOne(productDto.supplierId));
        // TODO CR check that the supplier is active!
        product.setCreateDate(LocalDateTime.now());
        productRepo.save(product);
        return product.getId();
    }

    public List<ProductSearchResult> searchProduct(ProductSearchCriteria criteria) {
        return productRepo.search(criteria);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void cheamMetodaAparentInocenta() {
        productRepo.save(new Product());
    }
}
