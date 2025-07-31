package victor.testing.design.fixturecreep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static victor.testing.design.fixturecreep.FeatureFlags.Feature.PORK_SHAWARMA;

//@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class ShawarmaServiceTest {
   @Mock // strict mock; see other ways to get strict mocks in the 'strictstubs' package
   Dependency dependency;
   @Mock
   FeatureFlags featureFlags;
   @InjectMocks
   ShawarmaService fastFood;

   @BeforeEach
   final void before() {
      // in before tii lucruri necesare tutoror± testelor
      when(dependency.isOnionAllowed()).thenReturn(true);
   }

   @Test
   void shawarmaTest() { // x 7 more tests
      when(featureFlags.isActive(PORK_SHAWARMA)).thenReturn(true);
      // ... complex
      fastFood.makeShawarma();
   }

}