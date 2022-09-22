package victor.testing.designhints.fixturecreep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShawarmaServiceTest {
   @Mock
   Dependency dependency;
   @InjectMocks
   KebabService kebabService;


   @BeforeEach
   final void before() {
      when(dependency.isOnionAllowed()).thenReturn(true);
   }

   @Test
   void shawarmaTest1() {
      kebabService.shawarma();
   } // + 5 tests like this

   @Test
   void shawarmaTest2() {
      kebabService.shawarma();
   } // + 5 tests like this

}
@ExtendWith(MockitoExtension.class)
class TzatzikiServiceTest {
   @Mock
   Dependency dependency;
   @InjectMocks
   TatzikiService tatzikiService;


   @BeforeEach
   final void before() {
      when(dependency.isCucumberAllowed()).thenReturn(true);
   }

   @Test
   void tzatzikiTest() {
      when(dependency.isCucumberAllowed()).thenReturn(true);

      tatzikiService.tzatziki();

      // ..
   } // + 5 tests like this
   @Test
   void tzatzikiTest2() {
      when(dependency.isCucumberAllowed()).thenReturn(true);

      tatzikiService.tzatziki();

      // ..
   } // + 5 tests like this
   @Test
   void tzatzikiTest3() {
      when(dependency.isCucumberAllowed()).thenReturn(true);

      tatzikiService.tzatziki();

      // ..
   } // + 5 tests like this
}