package victor.testing.design.fixturecreep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

//@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class TzatzikiTest {
  @Mock // strict mock; see other ways to get strict mocks in the 'strictstubs' package
  Dependency dependency;
  @InjectMocks
  FastFood fastFood;

  @BeforeEach
  final void before() {
    when(dependency.isCucumberAllowed()).thenReturn(true);
  }

  @Test
  void tzatzikiTest() { // + 5 more tests
    // ... complex
    fastFood.makeTzatziki();
  }
}