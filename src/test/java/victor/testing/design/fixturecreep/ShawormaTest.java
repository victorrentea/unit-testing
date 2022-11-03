package victor.testing.design.fixturecreep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT) // DO NOT DO THIS!
class ShawormaTest {
   @Mock
   Dependency dependency;
   @InjectMocks
   FastFood fastFood;

   @BeforeEach
   final void before() {
      // maybe one dark night . perhaps. For ONE call
      when(dependency.isOnionAllowed()).thenReturn(true);
   }

   @Test
   void shawarmaTest() { // + 7 more tests
      // ... complex
      fastFood.makeShawarma();
   }
   @Test
   void shawarmaTest7() { // + 7 more tests
      // ... complex
      fastFood.makeShawarma();
   }
   @Test
   void shawarmaTest6() { // + 7 more tests
      // ... complex
      fastFood.makeShawarma();
   }
}