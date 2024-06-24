package victor.testing.design.purity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mutation.Coupon;
import victor.testing.mutation.Customer;
import victor.testing.spring.domain.Product;
import victor.testing.spring.repo.ProductRepo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PriceServiceTest {

  @Mock
  private CustomerRepo customerRepo;

  @Mock
  private ThirdPartyPricesApi thirdPartyPricesApi;

  @Mock
  private CouponRepo couponRepo;

  @Mock
  private ProductRepo productRepo;

  @InjectMocks
  private PriceService priceService;

  @Test
  public void computesPricesWithInternalAndThirdPartyPrices() {
    long customerId = 1L;
    List<Long> productIds = Arrays.asList(1L, 2L);
    Map<Long, Double> internalPrices = new HashMap<>();
    internalPrices.put(1L, 100.0);
    internalPrices.put(2L, 200.0);

    when(customerRepo.findById(customerId)).thenReturn(new Customer());

    Product product1 = new Product();
    product1.setId(1L);
    Product product2 = new Product();
    product2.setId(2L);
    when(productRepo.findAllById(productIds)).thenReturn(Arrays.asList(product1, product2));

    Map<Long, Double> prices = priceService.computePrices(customerId, productIds, internalPrices);

  }

  @Test
  public void appliesAutoApplyCoupons() {
    long customerId = 1L;
    List<Long> productIds = Arrays.asList(1L);
    Map<Long, Double> internalPrices = new HashMap<>();
    internalPrices.put(1L, 100.0);

    Customer customer = new Customer();
    Coupon coupon = mock(Coupon.class);
    when(coupon.autoApply()).thenReturn(true);
    when(coupon.isApplicableFor(any(), anyDouble())).thenReturn(true);
    when(coupon.apply(any(), anyDouble())).thenReturn(80.0);
    customer.getCoupons().add(coupon);

    when(customerRepo.findById(customerId)).thenReturn(customer);
    when(productRepo.findAllById(productIds)).thenReturn(Arrays.asList(new Product()));

    Map<Long, Double> prices = priceService.computePrices(customerId, productIds, internalPrices);

  }

  @Test
  public void doesNotApplyNonAutoApplyCoupons() {
    long customerId = 1L;
    List<Long> productIds = Arrays.asList(1L);
    Map<Long, Double> internalPrices = new HashMap<>();
    internalPrices.put(1L, 100.0);

    Customer customer = new Customer();
    Coupon coupon = mock(Coupon.class);
    when(coupon.autoApply()).thenReturn(false);
    customer.getCoupons().add(coupon);

    when(customerRepo.findById(customerId)).thenReturn(customer);
    when(productRepo.findAllById(productIds)).thenReturn(Arrays.asList(new Product()));

    Map<Long, Double> prices = priceService.computePrices(customerId, productIds, internalPrices);

  }

  @Test
  public void doesNotApplySameCouponTwice() {
    long customerId = 1L;
    List<Long> productIds = Arrays.asList(1L, 2L);
    Map<Long, Double> internalPrices = new HashMap<>();
    internalPrices.put(1L, 100.0);
    internalPrices.put(2L, 200.0);

    Customer customer = new Customer();
    Coupon coupon = mock(Coupon.class);
    when(coupon.autoApply()).thenReturn(true);
    when(coupon.isApplicableFor(any(), anyDouble())).thenReturn(true);
    when(coupon.apply(any(), anyDouble())).thenReturn(80.0);
    customer.getCoupons().add(coupon);
    customer.getCoupons().add(coupon); // Add the same coupon twice

    when(customerRepo.findById(customerId)).thenReturn(customer);

    Product product1 = new Product();
    product1.setId(1L);
    Product product2 = new Product();
    product2.setId(2L);
    when(productRepo.findAllById(productIds)).thenReturn(Arrays.asList(product1, product2));

    Map<Long, Double> prices = priceService.computePrices(customerId, productIds, internalPrices);

  }

  @Test
public void doesNotApplyInapplicableCoupon() {
    long customerId = 1L;
    List<Long> productIds = Arrays.asList(1L);
    Map<Long, Double> internalPrices = new HashMap<>();
    internalPrices.put(1L, 100.0);

    Customer customer = new Customer();
    Coupon coupon = mock(Coupon.class);
    when(coupon.autoApply()).thenReturn(true);
    when(coupon.isApplicableFor(any(), anyDouble())).thenReturn(false); // The coupon is not applicable
    customer.getCoupons().add(coupon);

    when(customerRepo.findById(customerId)).thenReturn(customer);

    Product product = new Product();
    product.setId(1L);
    when(productRepo.findAllById(productIds)).thenReturn(Arrays.asList(product));

    Map<Long, Double> prices = priceService.computePrices(customerId, productIds, internalPrices);

}
}