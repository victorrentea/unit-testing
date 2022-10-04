package victor.testing.design.fixturecreep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

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