package victor.testing.design.fixturecreep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.*;
import static victor.testing.design.fixturecreep.FeatureFlags.Feature.PORK_SHAWARMA;


@MockitoSettings(strictness = Strictness.LENIENT) // dont !!
@ExtendWith(MockitoExtension.class)
class FastFoodTeRst {
   @Mock // strict mock; see other ways to get strict mocks in the 'strictstubs' package
   Dependency dependency;
   @Mock
   FeatureFlags featureFlags;
   @InjectMocks
   FastFood fastFood;

   @BeforeEach
   final void before() {
      when(dependency.isOnionAllowed()).thenReturn(true);
      when(featureFlags.isActive(PORK_SHAWARMA)).thenReturn(true);
      when(dependency.isCucumberAllowed()).thenReturn(true);
   }

   @Test
   void shawarmaTest() { // + 7 more tests
      // ... complex
      fastFood.makeShawarma();
   }
   @Test
   void shawarmaTest2() { // + 7 more tests
      // ... complex
      fastFood.makeShawarma();
   }
   @Test
   void shawarmaTest3() { // + 7 more tests
      // ... complex
      fastFood.makeShawarma();
   }



   @Test
   void tzatzikiTest() { // + 5 more tests
      // ... complex
      fastFood.makeTzatziki();
   }
   @Test
   void tzatzikiTest2() { // + 5 more tests
      // ... complex
      fastFood.makeTzatziki();
   }
   @Test
   void tzatzikiTest3() { // + 5 more tests
      // ... complex
      fastFood.makeTzatziki();
   }
}