package victor.testing.nested_fixtures;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.builder.Country;
import victor.testing.builder.Coupon;
import victor.testing.builder.Customer;
import victor.testing.builder.CustomerValidator;
import victor.testing.mocks.purity.CustomerRepo;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.tools.ReplaceCamelCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Disclaimer: Please do NOT take this example ad literam and replicate this level of granularity
 * unless you plan to get formal approval on its reading.
 * Could be a replacement for a .feature file?
 */
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceCamelCase.class)
class CreateCustomerShould {
   @Mock
   CustomerRepo customerRepo;
   @Mock
   EmailClient emailClient;

   CustomerFacade customerFacade;

   Customer aValidCustomer;
   @BeforeEach
   public final void before() {
      customerFacade = new CustomerFacade(new CustomerValidator(), customerRepo, emailClient);

      aValidCustomer = new Customer();
      aValidCustomer.setName("::name::");
      aValidCustomer.setEmail("::email::");
      aValidCustomer.getAddress().setCity("::city::");
      aValidCustomer.getAddress().setCountry(Country.ROU);
   }

   @Nested
   class GivenAnInvalidCustomer {

      @Test
      void failForMissingName() {
         aValidCustomer.setName(null);
         assertThatThrownBy(() -> customerFacade.createCustomer(aValidCustomer))
             .isInstanceOf(IllegalArgumentException.class);
      }
      @Test
      void failForMissingEmail() {
         aValidCustomer.setEmail(null);
         assertThatThrownBy(() -> customerFacade.createCustomer(aValidCustomer))
             .isInstanceOf(IllegalArgumentException.class);
      }

      @Test
      void failForMissingCity() {
         aValidCustomer.getAddress().setCity(null);
         assertThatThrownBy(() -> customerFacade.createCustomer(aValidCustomer))
             .isInstanceOf(IllegalArgumentException.class);
      }
      @Test
      void failForMissingCountry() {
         aValidCustomer.getAddress().setCountry(null);
         assertThatThrownBy(() -> customerFacade.createCustomer(aValidCustomer))
             .isInstanceOf(IllegalArgumentException.class);
      }
   }

   @Nested
   class GivenAValidCustomer {
      @BeforeEach
      public final void before() {
         when(customerRepo.countByEmail("::email::")).thenReturn(0);
      }
      @Test
      void failIfAnotherCustomerWithTheSameEmailExists() {
         when(customerRepo.countByEmail("::email::")).thenReturn(1);

         assertThatThrownBy(() -> customerFacade.createCustomer(aValidCustomer))
             .hasMessageContaining("already exists");
      }

      @Test
      void sendAWelcomeEmailAndBeSaveTheCustomer() {
         when(customerRepo.save(aValidCustomer)).thenReturn(13L);

         Long id = customerFacade.createCustomer(aValidCustomer);

         assertThat(id).isEqualTo(13L);
         verify(emailClient).sendWelcomeEmail(aValidCustomer);
      }

      @Nested
      class WithDiscountedCountry {
         @ParameterizedTest
         @ValueSource(strings = {"HOME","ELECTRONICS"})
         void receivesCoupon(ProductCategory category) {
            customerFacade.createCustomer(aValidCustomer);
            Assertions.assertThat(aValidCustomer.getCoupons())
                .contains(new Coupon(category, 10));
         }
         @Test
         void isSentAnEmailAboutTheCoupon() {
            customerFacade.createCustomer(aValidCustomer);
            verify(emailClient).sendNewCouponEmail(aValidCustomer);
         }
      }
   }
}