package victor.testing.design.fixturecreep;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static victor.testing.design.fixturecreep.FeatureFlags.Feature.PORK_SHAWARMA;


@ExtendWith(MockitoExtension.class) // exceptia apare doar daca folosesti @Mock / @InjectMock, nu =mock(
class FastFood_ShawarmaTest {
   @Mock // strict mock; see other ways to get strict mocks in the 'strictstubs' package
   Dependency dependency;
   @Mock
   FeatureFlags featureFlags;
   @InjectMocks
   FastFood fastFood;

   @BeforeEach
   final void before() {
      when(dependency.isOnionAllowed()).thenReturn(true);
      when(featureFlags.isActive(PORK_SHAWARMA)).thenReturn(true);
   }

   @Test
   void shawarmaTest() { // + 7 more tests
      // ... complex
      fastFood.makeShawarma();
   }

   @Test
   void shawarmaTest2() { // + 7 more tests
      // ... complex
      fastFood.makeShawarma();
   }

}

@ExtendWith(MockitoExtension.class)
class FastFood_TzatzikiTest {
   @Mock // strict mock; see other ways to get strict mocks in the 'strictstubs' package
   Dependency dependency;
   @InjectMocks
   FastFood fastFood;

//   @Before // JUnit 4 nu ruleaza daca @Test e din jupiter
   @BeforeEach // JUnit 5
   public final void before() {
      when(dependency.isCucumberAllowed()).thenReturn(true);
   }
   @Test
   void tzatzikiTest() { // + 5 more tests

      // ... complex
      fastFood.makeTzatziki();
   }
   @Test
   void tzatzikiTest2() { // + 5 more tests
      // ... complex
      fastFood.makeTzatziki();
   }
}