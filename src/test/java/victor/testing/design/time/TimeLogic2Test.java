package victor.testing.design.time;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TimeLogic2Test {
  OrderRepo orderRepoMock = mock(OrderRepo.class);
  TimeLogic2 target = new TimeLogic2(orderRepoMock);

  public static final String TEST_DATE = "2021-09-08";

  @Test
  void isFrequentBuyer() {
    LocalDate testDate = parse(TEST_DATE);
    when(orderRepoMock.findByCustomerIdAndCreatedOnBetween(
        13,
        parse("2021-09-01"),
        parse(TEST_DATE))).thenReturn(List.of(new Order().setTotalAmount(130d)));

    // PowerMock a murit, traiasca "mockito-inline" vezi pom.xml
    try (MockedStatic<LocalDate> staticMock = Mockito.mockStatic(LocalDate.class)) {
      staticMock.when(() -> LocalDate.now()).thenReturn(testDate);

      boolean result = target.isFrequentBuyer(13);

      assertThat(result).isTrue();
      System.out.println("Inainte:" + LocalDate.now());
    }
    // aici dupa, local Date Now iti da data curenta
    System.out.println("Dupa:" + LocalDate.now());

  }
}

// 1: inject a fixed Clock using TimeUtils
// 2: pass current date as an argument to a package-protected method (subcutaneous test)
// 3: mock statics LocalDate.now()
// 4: interface TimeProvider { LocalDate today(); } as a smell: **indirection without abstraction**
// 5: inject a Supplier<LocalDate>