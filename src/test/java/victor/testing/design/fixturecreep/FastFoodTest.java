//package victor.testing.design.fixturecreep;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class FastFoodTest {
//  @Mock
//  Dependency dependency;
//  @InjectMocks
//  FastFood fastFood;
//   @BeforeEach
//   final void before() {
//      System.out.println("run for both");
//   }
//
//  @Nested
//  class Shawarma {
//    @BeforeEach
//    final void before() {
//      when(dependency.isOnionAllowed()).thenReturn(true);
//    }
//
//    @Test
//    void shawarmaTest() { // + 7 more tests
//      // ... complex
//      fastFood.makeShawarma();
//    }
//
//  }
//
//  @Nested
//  class Tzatziki {
//    @BeforeEach
//    final void before() {
//      when(dependency.isCucumberAllowed()).thenReturn(true);
//    }
//
//    @Test
//    void tzatzikiTest() { // + 5 more tests
//      // ... complex
//      fastFood.makeTzatziki();
//    }
//  }
//
//
//}