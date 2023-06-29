package victor.testing.design.fixturecreep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT) // DON"T
// tells mockito to allow you to stub methods that are NOT really called by
   // the prod code you are testing !?
// WHAT'S WRONG WITH THAT?

class FastFoodShawarmaTest {
   @Mock // strict mock; see other ways to get strict mocks in the 'strictstubs' package
   Dependency dependency;
   @InjectMocks
   FastFood fastFood;

   @BeforeEach
   final void before() {
      when(dependency.isOnionAllowed()).thenReturn(true);
   }

   @Test
   void shawarmaTest() { // + 7 more tests
      // ... complex
      fastFood.makeShawarma();
   }

   @Test
   void shawarmaTest1() { // + 7 more tests
      // ... complex
      fastFood.makeShawarma();
   }

}

@ExtendWith(MockitoExtension.class)
class FastFoodTzatzikiTest {
   @Mock // strict mock; see other ways to get strict mocks in the 'strictstubs' package
   Dependency dependency;
   @InjectMocks
   FastFood fastFood;

   @BeforeEach
   final void before() {
      when(dependency.isCucumberAllowed()).thenReturn(true);
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
}