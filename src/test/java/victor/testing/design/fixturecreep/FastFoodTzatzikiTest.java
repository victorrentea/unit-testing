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
   Dependency dependency;
   @InjectMocks
   FastFoodTzatziki fastFood;

   @BeforeEach
   final void before() {
      when(dependency.isCucumberAllowed()).thenReturn(true);
   }

   @Test
   void tzatzikiTest() { // + 5 more tests
      // ... complex
      fastFood.makeTzatziki();
   }
}