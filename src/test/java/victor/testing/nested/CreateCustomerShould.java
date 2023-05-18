package victor.testing.nested;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mutation.Country;
import victor.testing.mutation.Coupon;
import victor.testing.mutation.Customer;
import victor.testing.mutation.CustomerValidator;
import victor.testing.design.purity.CustomerRepo;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.tools.HumanReadableTestNames;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Disclaimer: Please do NOT take this example ad literam and replicate this level of granularity
 * unless you plan to get formal approval on its reading.
 * Could be a replacement for a .feature file?
 */
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(HumanReadableTestNames.class)
class CreateCustomerShould {
   @Mock
   CustomerRepo customerRepo;
   @Mock
   EmailClient emailClient;

   CustomerFacade sut;

   Customer aValidCustomer = new Customer();
   @BeforeEach
   public final void before() {
      // unitare sociale: testez facade + validator incojurate de mockuri
      sut = new CustomerFacade(new CustomerValidator(), customerRepo, emailClient);
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
         assertThatThrownBy(() -> sut.createCustomer(aValidCustomer))
             .isInstanceOf(IllegalArgumentException.class);
      }
      @Test
      void failForMissingEmail() {
         aValidCustomer.setEmail(null);
         assertThatThrownBy(() -> sut.createCustomer(aValidCustomer))
             .isInstanceOf(IllegalArgumentException.class);
      }

      @Test
      void failForMissingCity() {
         aValidCustomer.getAddress().setCity(null);
         assertThatThrownBy(() -> sut.createCustomer(aValidCustomer))
             .isInstanceOf(IllegalArgumentException.class);
      }
      @Test
      void failForMissingCountry() {
         aValidCustomer.getAddress().setCountry(null);
         assertThatThrownBy(() -> sut.createCustomer(aValidCustomer))
             ;
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

         assertThatThrownBy(() -> sut.createCustomer(aValidCustomer))
             .hasMessageContaining("already exists");
      }

      @Test
      void sendAWelcomeEmailAndBeSaveTheCustomer() {
         when(customerRepo.save(aValidCustomer)).thenReturn(13L);

         Long id = sut.createCustomer(aValidCustomer);

         assertThat(id).isEqualTo(13L);
         verify(emailClient).sendWelcomeEmail(aValidCustomer);
      }

      @Nested
      class WithDiscountedCountry {
         @ParameterizedTest
         @ValueSource(strings = {"HOME","ELECTRONICS"})
         void receivesCoupon(ProductCategory category) {
            sut.createCustomer(aValidCustomer);
            Assertions.assertThat(aValidCustomer.getCoupons())
                .contains(new Coupon(category, 10, Set.of()));
         }
         @Test
         void isSentAnEmailAboutTheCoupon() {
            sut.createCustomer(aValidCustomer);
            verify(emailClient).sendNewCouponEmail(aValidCustomer);
         }
      }
   }
}