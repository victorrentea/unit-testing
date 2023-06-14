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
   @Mock // strict mock; see other ways to get strict mocks in the 'strictstubs' package
   Dependency dependency;
   @InjectMocks
   FastFood fastFood;

   @BeforeEach
   final void before() {
      // test fixture creep
      when(dependency.isOnionAllowed()).thenReturn(true);
      when(dependency.isCucumberAllowed()).thenReturn(true);
      when(dependency.isOnionAllowed()).thenReturn(true);
      when(dependency.isCucumberAllowed()).thenReturn(true);
      when(dependency.isOnionAllowed()).thenReturn(true);
      when(dependency.isCucumberAllowed()).thenReturn(true);
      when(dependency.isOnionAllowed()).thenReturn(true);
      when(dependency.isCucumberAllowed()).thenReturn(true);
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