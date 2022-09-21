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
import victor.testing.mutation.*;
import victor.testing.designhints.purity.CustomerRepo;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.tools.ReplaceCamelCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static victor.testing.mutation.TestData.anAddress;
import static victor.testing.mutation.TestData.john;

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

   @BeforeEach
   public final void before() {
      customerFacade = new CustomerFacade(new CustomerValidator(), customerRepo, emailClient);

   }

   @Nested
   class GivenAnInvalidCustomer {

      @Test
      void failForMissingName() {
         Customer customer = john().name(null).build();
         assertThatThrownBy(() -> customerFacade.createCustomer(customer))
             .isInstanceOf(IllegalArgumentException.class);
      }
      @Test
      void failForMissingEmail() {
         Customer customer = john().email(null).build();
         assertThatThrownBy(() -> customerFacade.createCustomer(customer))
             .isInstanceOf(IllegalArgumentException.class);
      }

      @Test
      void failForMissingCity() {
         Customer customer = john().address(anAddress().city(null).build()).build();
         assertThatThrownBy(() -> customerFacade.createCustomer(customer))
             .isInstanceOf(IllegalArgumentException.class);
      }
      @Test
      void failForMissingCountry() {
         Customer customer = john().address(anAddress().country(null).build()).build();
         assertThatThrownBy(() -> customerFacade.createCustomer(customer))
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

         assertThatThrownBy(() -> customerFacade.createCustomer(john().build()))
             .hasMessageContaining("already exists");
      }

      @Test
      void sendAWelcomeEmailAndBeSaveTheCustomer() {
         Customer john = john().build();
         when(customerRepo.save(john)).thenReturn(13L);

         Long id = customerFacade.createCustomer(john);

         assertThat(id).isEqualTo(13L);
         verify(emailClient).sendWelcomeEmail(john);
      }

      @Nested
      class WithDiscountedCountry {
         @ParameterizedTest
         @ValueSource(strings = {"HOME","ELECTRONICS"})
         void receivesCoupon(ProductCategory category) {
            Customer john = john().build();
            customerFacade.createCustomer(john);
            Assertions.assertThat(john.getCoupons())
                .contains(new Coupon(category, 10));
         }
         @Test
         void isSentAnEmailAboutTheCoupon() {
            Customer john = john().build();
            customerFacade.createCustomer(john);
            verify(emailClient).sendNewCouponEmail(john);
         }
      }
   }
}