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

   }

   @Test
   void complex1() {
      when(dependency.isOnionAllowed()).thenReturn(true);

      kebab.complex1();

      // ..
   } // + 5 tests like this

   @Test
   void complex2() {
      when(dependency.isCucumberAllowed()).thenReturn(true);

      kebab.complex2();

      // ..
   } // + 5 tests like this
}