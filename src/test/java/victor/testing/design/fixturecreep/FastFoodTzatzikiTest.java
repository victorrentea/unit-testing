package victor.testing.design.fixturecreep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FastFoodTzatzikiTest {
   @Mock
   Dependency dependency = mock(Dependency.class);
   // under the hood, Mockito generates a proxy (a subclass)
   //    in memory that it loads and serves instead of the real one
   // that's why you cannot mock final classes or methods.
//   Dependency dependency = new Dependency() {
//      @Override
//      public boolean isOnionAllowed() {
//         return false;
//      }
//      @Override
//      public boolean isCucumberAllowed() {
//         return true;
//      }
//   }
   @InjectMocks
   FastFoodTzatziki fastFood;

   @BeforeEach
   final void before() {
//      System.out.println(dependency.getClass());
      when(dependency.isCucumberAllowed()).thenReturn(true);
   }

   @Test
   void tzatzikiTest() { // + 5 more tests
      // ... complex
      fastFood.makeTzatziki();
   }
}