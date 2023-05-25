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


//@MockitoSettings(strictness = Strictness.LENIENT) // NOT
//@ExtendWith(MockitoExtension.class)
class FastFoodTest {
//   @Mock // strict mock; see other ways to get strict mocks in the 'strictstubs' package
   Dependency dependency = mock(Dependency.class);
//   @InjectMocks
   FastFood fastFood = new FastFood(dependency);

   @BeforeEach
   final void before() {
      // bad practice to accumulate stuff shared by a subset (<70% of tests)
      /*lenient().*/when(dependency.isOnionAllowed()).thenReturn(true);
      when(dependency.isCucumberAllowed()).thenReturn(true);
   }

   @Test
   void shawarmaTest() { // + 7 more tests imagine
      // ... complex
      fastFood.makeShawarma();
   }

   @Test
   void tzatzikiTest() { // + 5 more tests
      // ... complex
      fastFood.makeTzatziki();
   }
}