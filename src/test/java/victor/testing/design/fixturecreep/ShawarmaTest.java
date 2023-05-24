package victor.testing.design.fixturecreep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;


//@MockitoSettings(strictness = Strictness.LENIENT) // DON"T DO THIS, you are back 10y ago with @Before of dozens of lines
@ExtendWith(MockitoExtension.class)
class ShawarmaTest {
   @Mock // strict mock; see other ways to get strict mocks in the 'strictstubs' package
   Dependency dependency;
   @InjectMocks
   ShawarmaService target;

   @BeforeEach
   final void before() {
      // the before should be used by 95% of the tests in a test class
      when(dependency.isOnionAllowed()).thenReturn(true);
   }

   @Test
   void shawarmaTest() { // + 7 more tests
      // ... complex
      target.makeShawarma();
   }

}