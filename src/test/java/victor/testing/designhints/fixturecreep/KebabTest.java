package victor.testing.designhints.fixturecreep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KebabTestComplex1 {
   @Mock
   Dependency dependency;
   @InjectMocks
   Kebab kebab;

   @BeforeEach
   final void before() {
      when(dependency.isOnionAllowed()).thenReturn(true);
   }

   @Test
   void complex1() {

      kebab.complex1();

      // ..
   } // + 5 tests like this
}
   @ExtendWith(MockitoExtension.class)
class KebabTestComplex2 {
      @Mock
      Dependency dependency;
      @InjectMocks
      KebabComplex2 kebab;
      @BeforeEach //specific to all the tests
      final void before() {
         when(dependency.isCucumberAllowed()).thenReturn(true);
      }
   @Test
   void complex2() {
      kebab.complex2();
   }

   @Test
   void complex2a() {
      kebab.complex2();
   }

   @Test
   void complex2b() {
      kebab.complex2();
   }

   @Test
   void complex2c() {
      kebab.complex2();
   }

   @Test
   void complex2d() {
      kebab.complex2();
   }

   @Test
   void complex2e() {
      kebab.complex2();
   }
}