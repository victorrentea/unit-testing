package victor.testing.nested;

import lombok.RequiredArgsConstructor;
import victor.testing.mutation.Country;
import victor.testing.mutation.Coupon;
import victor.testing.mutation.Customer;
import victor.testing.mutation.CustomerValidator;
import victor.testing.design.purity.CustomerRepo;
import victor.testing.spring.entity.ProductCategory;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class CustomerFacade {
   private final CustomerValidator validator;
   private final CustomerRepo customerRepo;
   private final EmailClient emailClient;

   private static final List<Country> DISCOUNTED_COUNTRIES = List.of(Country.ROU, Country.BGR, Country.SRB);

   public Long createCustomer(Customer customer) {
      validator.validate(customer);

      if (customerRepo.countByEmail(customer.getEmail()) != 0) {
         throw new IllegalArgumentException("A customer with that email already exists");
      }

      emailClient.sendWelcomeEmail(customer);

      if (DISCOUNTED_COUNTRIES.contains(customer.getAddress().getCountry())) {
         customer.getCoupons().add(new Coupon(ProductCategory.ELECTRONICS, 10, Set.of()));
         customer.getCoupons().add(new Coupon(ProductCategory.HOME, 10, Set.of()));
         emailClient.sendNewCouponEmail(customer);
      }

      Long id = customerRepo.save(customer);

      return id;
   }

}
