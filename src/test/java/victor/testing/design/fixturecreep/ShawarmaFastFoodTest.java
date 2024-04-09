package victor.testing.design.fixturecreep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static victor.testing.design.fixturecreep.FeatureFlags.Feature.PORK_SHAWARMA;


@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT) // ilegal in teste noi: pt ca face testele nementenabile
class ShawarmaFastFoodTest {
   @Mock // strict mock; see other ways to get strict mocks in the 'strictstubs' package
   Dependency dependency;
   @Mock
   FeatureFlags featureFlags;
   @InjectMocks
   FastFood fastFood;

   @BeforeEach
   final void before() {
      when(dependency.isOnionAllowed()).thenReturn(true);
      // optional call, nu moare nimeni daca nu e chemata
      lenient().when(featureFlags.isActive(PORK_SHAWARMA)).thenReturn(true);
   }

   @Test
   void shawarmaTest() { // + 7 more tests
      // ... complex
      fastFood.makeShawarma(true);
   }

   @Test
   void shawarmaTest2() { // + 7 more tests
      // ... complex
      fastFood.makeShawarma(true);
   }
   @Test
   void shawarmaTestVeggie() { // + 7 more tests
      // ... complex
      fastFood.makeShawarma(false);
   }
}
