package victor.testing.design.fixturecreep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT) // DO NOT DO THIS!
class TzatzikiTest {
   @Mock
   Dependency dependency;
   @InjectMocks
   FastFood fastFood;

   @BeforeEach
   final void before() {
      // maybe one dark night . perhaps. For ONE call
      when(dependency.isCucumberAllowed()).thenReturn(true);
   }

   @Test
   void tzatzikiTest() { // + 5 more tests
      // ... complex
      fastFood.makeTzatziki();
   }
   @Test
   void tzatzikiTest1() { // + 5 more tests
      // ... complex
      fastFood.makeTzatziki();
   }
   @Test
   void tzatzikiTest2() { // + 5 more tests
      // ... complex
      fastFood.makeTzatziki();
   }
}