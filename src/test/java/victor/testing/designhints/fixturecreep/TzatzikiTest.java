package victor.testing.designhints.fixturecreep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TzatzikiTest {
   @Mock
   Dependency dependency;
   @InjectMocks
   Kebab kebab;

   @BeforeEach
   final void before() {
      when(dependency.isCucumberAllowed()).thenReturn(true);
   }

   @Test
   void tzatziki() {

      kebab.tzatziki();
   } // + 5 tests like this
   @Test
   void tzatziki2() {

      kebab.tzatziki();
   } // + 5 tests like this
}