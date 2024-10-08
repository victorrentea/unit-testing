package victor.testing.nested;

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
import victor.testing.spring.entity.ProductCategory;
import victor.testing.tools.PrettyTestNames;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Disclaimer: Useful if you plan to get a non-technical to approval its output.
 * Could this be an alternative to .feature file? (without the fun of writing it together)
 */
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(PrettyTestNames.class)
class CreateCustomerShould {
  @Mock
  CustomerRepo customerRepo;
  @Mock
  EmailClient emailClient;

  CustomerFacade customerFacade;

  // ⚠️ JUnit creates a new test class instance for each @Test
  Customer aCustomer = new Customer()
      .setName("::name::")
      .setEmail("::email::")
      .setAddress(new Address()
          .setCity("::city::")
          .setCountry(Country.ESP)
      );

  @BeforeEach
  public final void before() {
    // social unit tests: testing ( Facade + Validator ) surrounded by mocks
    customerFacade = new CustomerFacade(new CustomerValidator(), customerRepo, emailClient);
  }

  @Nested
  class FailIf {
    @Test
    void missingName() {
      aCustomer.setName(null);
      assertThatThrownBy(() -> customerFacade.createCustomer(aCustomer))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void missingEmail() {
      aCustomer.setEmail(null);
      assertThatThrownBy(() -> customerFacade.createCustomer(aCustomer))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void missingCity() {
      aCustomer.getAddress().setCity(null);
      assertThatThrownBy(() -> customerFacade.createCustomer(aCustomer))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cityTooShort() {
      aCustomer.getAddress().setCity("aa");
      assertThatThrownBy(() -> customerFacade.createCustomer(aCustomer))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void anotherCustomerWithTheSameEmailExists() {
      when(customerRepo.countByEmail("::email::")).thenReturn(1);

      assertThatThrownBy(() -> customerFacade.createCustomer(aCustomer))
          .hasMessageContaining("already exists");
    }
  }

  @Nested
  class ForAValidCustomer {
    public static final long NEW_CUSTOMER_ID = 13L;

    @BeforeEach
    public final void before() {
      when(customerRepo.countByEmail("::email::")).thenReturn(0);
      when(customerRepo.save(aCustomer)).thenReturn(NEW_CUSTOMER_ID);
    }

    @Test
    void saveTheCustomer() {
      Long id = customerFacade.createCustomer(aCustomer);

      assertThat(id).isEqualTo(NEW_CUSTOMER_ID);
    }

    @Test
    void sendAWelcomeEmail() {
      customerFacade.createCustomer(aCustomer);

      verify(emailClient).sendWelcomeEmail(aCustomer);
    }

    @Nested
    class WithDiscountedCountry {
      @BeforeEach
      final void setup() {
        aCustomer.getAddress().setCountry(Country.ROU);
      }

      @ParameterizedTest(name = "for {0} category")
      @ValueSource(strings = {"HOME", "ELECTRONICS"})
      void receivesCoupon(ProductCategory category) {
        customerFacade.createCustomer(aCustomer);

        assertThat(aCustomer.getCoupons())
            .contains(new Coupon(category, 10, Set.of()));
      }

      @Test
      void isSentAnEmailAboutTheCoupons() {
        customerFacade.createCustomer(aCustomer);

        verify(emailClient).sendNewCouponEmail(aCustomer);
      }
    }
  }
}