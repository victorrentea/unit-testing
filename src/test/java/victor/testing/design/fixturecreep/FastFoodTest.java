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


//@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)  // DON'T USE #1
// // for migrating old tests allows again horror Before with 10+ when...then
class FastFoodTest {
   @Mock // strict mock; see other ways to get strict mocks in the 'strictstubs' package
   Dependency dependency;// = mock(Dependency.class); // not using @ for mockito allows a when..then not to be used
   @InjectMocks
   FastFood fastFood;// = new FastFood(dependency);

   @BeforeEach
   final void before() {
      // test fixture creep
//      lenient(). // #3 selectively allow one stubbing to be NOT used;
          // acceptable when stubbing feature flags:
              // lenient().when(featureService.hasFeature(TURK_KEBAB)).thenReturn(true);
       when(dependency.isOnionAllowed()).thenReturn(true);
       when(dependency.isCucumberAllowed()).thenReturn(true);
   }

   @Test
   void shawarmaTest() { // + 7 more tests
      // ... complex
      fastFood.makeShawarma();
   }

   @Test
   void tzatzikiTest() { // + 5 more tests
      // ... complex
      fastFood.makeTzatziki();
   }
}