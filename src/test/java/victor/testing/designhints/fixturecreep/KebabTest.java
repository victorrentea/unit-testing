package victor.testing.designhints.fixturecreep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShawarmaTest {
   @Mock
   Dependency dependency;
   @InjectMocks
   Shawarma kebab;

   @BeforeEach
   final void before() {
      when(dependency.isOnionAllowed()).thenReturn(true);
   }
   @Test
   void shawarma() {
      kebab.shaworma();
   } // + 5 tests like this
   @Test
   void shawarma1() {
      kebab.shaworma();
   } // + 5 tests like this
   @Test
   void shawarma2() {
      kebab.shaworma();
   } // + 5 tests like this
   @Test
   void shawarma3() {
      kebab.shaworma();
   } // + 5 tests like this

}
@ExtendWith(MockitoExtension.class)
class TzatzikiTest {
   @Mock
   Dependency dependency;
   @InjectMocks
   Tzatziki kebab;

   @BeforeEach
   final void before() {
      when(dependency.isCucumberAllowed()).thenReturn(true);

   }
   @Test
   void complex2() {
      when(dependency.isCucumberAllowed()).thenReturn(true);

      kebab.tzatziki();

      // ..
   } // + 5 tests like this
}