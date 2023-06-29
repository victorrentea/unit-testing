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


@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT) // DON"T
// tells mockito to allow you to stub methods that are NOT really called by
   // the prod code you are testing !?
// WHAT'S WRONG WITH THAT?

class FastFoodTest {
   @Mock // strict mock; see other ways to get strict mocks in the 'strictstubs' package
   Dependency dependency;
   @InjectMocks
   FastFood fastFood;

   @BeforeEach
   final void before() {
      // 2 years laters, withe LENIENT MOCKS
      // WHich of these 20 lines of before is used by the FAILED test I'm investigating!?
      when(dependency.isOnionAllowed()).thenReturn(true);
      when(dependency.isCucumberAllowed()).thenReturn(true);
      // keep in the before ONLY stuff used by ALL tests below
   }
   //

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