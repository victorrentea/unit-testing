package victor.testing.nested;

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
import victor.testing.design.purity.CustomerRepo;
import victor.testing.mutation.*;
import victor.testing.spring.product.domain.ProductCategory;
import victor.testing.tools.HumanReadableTestNames;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

  CustomerFacade customerFacade;

  Customer aValidCustomer = TestData.musk();

  @BeforeEach // hierarchical test fixtures
  public final void before() {
    // social unit tests: testing ( Facade + Validator ) surrounded by mocks
    customerFacade = new CustomerFacade(new CustomerValidator(), customerRepo, emailClient);
//    when()
  }

  @Nested
  class FailForInvalidCustomer {
    @Test
    void missingName() {
      aValidCustomer.setName(null);
      assertThatThrownBy(() -> customerFacade.createCustomer(aValidCustomer))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void missingEmail() {
      aValidCustomer.setEmail(null);
      assertThatThrownBy(() -> customerFacade.createCustomer(aValidCustomer))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void missingCity() {
      aValidCustomer.getAddress().setCity(null);
      assertThatThrownBy(() -> customerFacade.createCustomer(aValidCustomer))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cityTooShort() {
      aValidCustomer.getAddress().setCity("aa");
      assertThatThrownBy(() -> customerFacade.createCustomer(aValidCustomer))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  class ForAValidCustomer {
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
      @ValueSource(strings = {"HOME", "ELECTRONICS"})
      void receivesCoupon(ProductCategory category) {
        customerFacade.createCustomer(aValidCustomer);
        Assertions.assertThat(aValidCustomer.getCoupons())
            .contains(new Coupon(category, 10, Set.of()));
      }

      @Test
      void isSentAnEmailAboutTheCoupons() {
        customerFacade.createCustomer(aValidCustomer);
        verify(emailClient).sendNewCouponEmail(aValidCustomer);
      }
    }
  }
}