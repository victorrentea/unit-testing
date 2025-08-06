package victor.testing.design.fixturecreep;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TzatzikiServiceTest {
   @Mock // strict mock; see other ways to get strict mocks in the 'strictstubs' package
   Dependency dependency;
   @InjectMocks
   TzatzikiService fastFood;
   @Test
   void tzatzikiTest() { // + 5 more tests
      when(dependency.isCucumberAllowed()).thenReturn(true);
      // ... complex
      fastFood.makeTzatziki();
   }
}