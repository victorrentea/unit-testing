package victor.testing.design.fixturecreep;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static victor.testing.design.fixturecreep.FeatureFlags.Feature.PORK_SHAWARMA;

//@MockitoSettings(strictness = Strictness.LENIENT) // DONT. Fix1
// it makes understanding failed tests HARDER, because of that unnecessary stubbing
@ExtendWith(MockitoExtension.class)
class ShawarmaServiceTest {
   @Mock // strict mock; see other ways to get strict mocks in the 'strictstubs' package
   Dependency dependency;
   @Mock
   FeatureFlags featureFlags;
   @InjectMocks
   ShawarmaService fastFood;

   @BeforeEach
   final void before() {
      when(dependency.isOnionAllowed()).thenReturn(true);
      // because I don't care if you asked for the flags
      lenient().when(featureFlags.isActive(PORK_SHAWARMA)).thenReturn(true);
   }

   @Test
   void shawarmaTestThrows() { // + 7 more tests
      when(dependency.isOnionAllowed()).thenReturn(false);

      Assertions.assertThatThrownBy(() -> fastFood.makeShawarma())
          .isInstanceOf(IllegalArgumentException.class);
   }

   @Test
   void shawarmaTest() { // + 7 more tests

      fastFood.makeShawarma();
   }

   @Test
   void shawarmaTest2() { // + 7 more tests

      fastFood.makeShawarma();
   }

   @Test
   void shawarmaTest3() { // + 7 more tests

      fastFood.makeShawarma();
   }
}

