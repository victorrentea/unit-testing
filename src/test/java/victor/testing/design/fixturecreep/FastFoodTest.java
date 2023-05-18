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

//@MockitoSettings(strictness = Strictness.LENIENT)
//@ExtendWith(MockitoExtension.class)
class FastFoodTest {
//   @Mock // strict mock; see other ways to get strict mocks in the 'strictstubs' package
   Dependency dependency = mock(Dependency.class);
   //   @InjectMocks
   FastFood fastFood = new FastFood(dependency);

   @BeforeEach
   final void before() {
      // pe stub il inveti ce sa returneze vs mock
      /*lenient().*/when(dependency.isOnionAllowed()).thenReturn(true); // eu stabuiesc o metoda
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