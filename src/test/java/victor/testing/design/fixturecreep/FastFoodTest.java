package victor.testing.design.fixturecreep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

// .mock() method builds lenient mocks by default
//   Dependency dependency = mock(Dependency.class, withSettings().strictness(STRICT_STUBS)); // Mockito 4.6.0+
//   FastFood fastFood = new FastFood(dependency);

@ExtendWith(MockitoExtension.class)
class FastFoodTest {
   @Mock
   Dependency dependency;
   @InjectMocks
   FastFood fastFood;

   @BeforeEach
   final void before() {
   }

   @Test
   void shawarmaTest() { // + 7 more tests
      when(dependency.isOnionAllowed()).thenReturn(true);
      // ... complex
      fastFood.makeShawarma();
   }

   @Test
   void tzatzikiTest() { // + 5 more tests
      when(dependency.isCucumberAllowed()).thenReturn(true);
      // ... complex
      fastFood.makeTzatziki();
   }
}