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
   Kebab kebab;

   @BeforeEach
   final void before() {
      when(dependency.isOnionAllowed()).thenReturn(true);
   }

   @Test
   void shawarma() {
      kebab.shawarma();
   } // + 5 tests like this
   @Test
   void shawarma2() {
      kebab.shawarma();
   } // + 5 tests like this

}