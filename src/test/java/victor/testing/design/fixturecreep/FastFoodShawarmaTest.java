package victor.testing.design.fixturecreep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static victor.testing.design.fixturecreep.FeatureFlags.Feature.PORK_SHAWARMA;

//@MockitoSettings(strictness = LENIENT) RAU!!!=PR reject
@ExtendWith(MockitoExtension.class)
class FastFoodShawarmaTest {
   @Mock // strict mock; see other ways to get strict mocks in the 'strictstubs' package
   Dependency dependency;
   @Mock
   FeatureFlags featureFlags;
   @InjectMocks
   ShawarmaLaBaiatu sut;

   @BeforeEach
   final void before() {
      when(dependency.isOnionAllowed()).thenReturn(true);
      lenient().when(featureFlags.isActive(PORK_SHAWARMA)).thenReturn(true);
   }

   @Test
   void shawarmaDeBirou() { // + 7 more tests
      // ... complex
      when(dependency.isOnionAllowed()).thenReturn(false);

      assertThrows(IllegalArgumentException.class, () -> sut.makeShawarma());
   }
   @Test
   void shawarmaTest() { // + 7 more tests
      // ... complex
      sut.makeShawarma();
   }
   @Test
   void shawarmaTest1() { // + 7 more tests
      // ... complex
      sut.makeShawarma();

      verify(dependency, never()).reportFraud(); // nici o alta metoda nu a fost chemata
//        verifyNoMoreInteractions(dependency); // nici o alta metoda nu a fost chemata
   }
   @Test
   void shawarmaTest2() { // + 7 more tests
      // ... complex
      sut.makeShawarma();
   }
}