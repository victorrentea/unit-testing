package victor.testing.design.purity;

import org.junit.jupiter.api.Test;
import victor.testing.mutation.Coupon;
import victor.testing.mutation.Customer;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;
import victor.testing.spring.entity.Supplier;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.spring.entity.ProductCategory.*;

class AltaClasaInProdTest {
    
    private final AltaClasaInProd altaClasaInProd = new AltaClasaInProd();
    
    @Test
    void applyCoupons_appliesMatchingCoupons() {
        // arrange
        Coupon coupon1 = new Coupon(HOME, 2, Set.of(13L));
        Coupon coupon2 = new Coupon(ELECTRONICS, 4, Set.of(13L));
        Customer customer = new Customer().setCoupons(List.of(coupon1, coupon2));
        
        Product p1 = new Product().setId(1L).setCategory(HOME).setSupplier(new Supplier().setId(13L));
        Product p2 = new Product().setId(2L).setCategory(KIDS).setSupplier(new Supplier().setId(13L));
        
        Map<Long, Double> resolvedPrices = Map.of(1L, 10d, 2L, 5d);
        
        // act
        AltaClasaInProd.ApplyCouponResult result = altaClasaInProd.applyCoupons(
            List.of(p1, p2), resolvedPrices, customer);
        
        // assert
        assertThat(result.usedCoupons()).containsExactly(coupon1);
        assertThat(result.finalPrices())
            .containsEntry(1L, 8d)
            .containsEntry(2L, 5d);
    }
    
    @Test
    void applyCoupons_noMatchingCoupons_returnsSamePrices() {
        // arrange
        Coupon coupon = new Coupon(ELECTRONICS, 2, Set.of(13L));
        Customer customer = new Customer().setCoupons(List.of(coupon));
        
        Product product = new Product().setId(1L).setCategory(HOME).setSupplier(new Supplier().setId(13L));
        Map<Long, Double> resolvedPrices = Map.of(1L, 10d);
        
        // act
        AltaClasaInProd.ApplyCouponResult result = altaClasaInProd.applyCoupons(
            List.of(product), resolvedPrices, customer);
        
        // assert
        assertThat(result.usedCoupons()).isEmpty();
        assertThat(result.finalPrices()).containsEntry(1L, 10d);
    }
    
    @Test
    void applyCoupons_multipleCouponsApplied_usesEachCouponOnce() {
        // arrange
        Coupon coupon1 = new Coupon(HOME, 2, Set.of(13L));
        Coupon coupon2 = new Coupon(HOME, 3, Set.of(13L));
        Customer customer = new Customer().setCoupons(List.of(coupon1, coupon2));
        
        Product p1 = new Product().setId(1L).setCategory(HOME).setSupplier(new Supplier().setId(13L));
        Product p2 = new Product().setId(2L).setCategory(HOME).setSupplier(new Supplier().setId(13L));
        
        Map<Long, Double> resolvedPrices = Map.of(1L, 10d, 2L, 20d);
        
        // act
        AltaClasaInProd.ApplyCouponResult result = altaClasaInProd.applyCoupons(
            List.of(p1, p2), resolvedPrices, customer);
        
        // assert
        // Both coupons are being applied to product 1 because:
        // 1. coupon1 (discount 2) is applied first, reducing price from 10 to 8
        // 2. coupon2 (discount 3) is then applied, further reducing price from 8 to 5
        // The second product price remains unchanged at 20
        assertThat(result.usedCoupons()).containsExactly(coupon1, coupon2);
        assertThat(result.finalPrices())
            .containsEntry(1L, 5d)
            .containsEntry(2L, 20d);
    }
}