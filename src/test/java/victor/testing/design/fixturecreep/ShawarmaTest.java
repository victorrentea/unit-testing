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
class ShawarmaTest {
  @Mock // strict mock; see other ways to get strict mocks in the 'strictstubs' package
  Dependency dependency;
  @InjectMocks
  Shawarma shawarma;

  @BeforeEach
  final void before() {
    when(dependency.isOnionAllowed()).thenReturn(true); // eu stabuiesc o metoda
  }

  @Test
  void shawarmaTest() { // + 7 more tests
    // ... complex
    shawarma.makeShawarma();
  }

}