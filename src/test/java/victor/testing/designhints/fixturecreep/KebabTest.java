package victor.testing.designhints.fixturecreep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KebabTest {
   @Mock
   Dependency dependency;
   @InjectMocks
   Kebab kebab;

   @BeforeEach
   final void before() {
      when(dependency.isOnionAllowed()).thenReturn(true);
      when(dependency.isCucumberAllowed()).thenReturn(true);
   }

   @Test
   void complex1() { // x 7
      kebab.complex1();

      // ..
   } // + 5 tests like this

   @Test
   void complex2() { // x 7


      kebab.complex2();

      // ..
   } // + 5 tests like this
}