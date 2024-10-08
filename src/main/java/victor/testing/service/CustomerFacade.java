package victor.testing.service;

import lombok.RequiredArgsConstructor;
import victor.testing.entity.Country;
import victor.testing.entity.Coupon;
import victor.testing.entity.Customer;
import victor.testing.entity.ProductCategory;
import victor.testing.repo.CustomerRepo;
import victor.testing.repo.EmailClient;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class CustomerFacade {
   private final CustomerValidator customerValidator;
   private final CustomerRepo customerRepo;
   private final EmailClient emailClient;

   private static final List<Country> DISCOUNTED_COUNTRIES = List.of(Country.ROU, Country.BEL, Country.SRB);

   public Long createCustomer(Customer customer) {
      customerValidator.validate(customer);

      if (customerRepo.countByEmail(customer.getEmail()) != 0) {
         throw new IllegalArgumentException("A customer with that email already exists");
      }

      emailClient.sendWelcomeEmail(customer);

      if (DISCOUNTED_COUNTRIES.contains(customer.getAddress().getCountry())) {
         customer.getCoupons().add(new Coupon(ProductCategory.ELECTRONICS, 10, Set.of()));
         customer.getCoupons().add(new Coupon(ProductCategory.HOME, 10, Set.of()));
         emailClient.sendNewCouponEmail(customer);
      }

      return customerRepo.save(customer).getId();
   }
}
