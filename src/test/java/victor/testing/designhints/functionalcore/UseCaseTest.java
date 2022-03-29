package victor.testing.designhints.functionalcore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UseCaseTest {
   @Mock RepoA repoA;
   @Mock RepoB repoB;
   @Mock RepoC repoC;
   // option 1: social unit tests: some dep are REAL ONES
   // option 2: black box test it all:
   FunctionalCore functionalCore = new FunctionalCore();
   @InjectMocks ImperativeShell target;
   @Captor ArgumentCaptor<C> cCaptor;

   @BeforeEach
   final void before() {

   }
   @Test
   void case51() {
      A a = new A("ax");
      B b = new B("bx");
      when(repoA.findById(1L)).thenReturn(a);
      when(repoB.findById(2L)).thenReturn(b);
      C c = new C("a");
      when(functionalCore.pureFunction(a,b)).thenReturn(c);

      target.case51(1L, 2L);

      verify(repoC).save(c);
   }

   // TODO write 6 more such tests, to explore other paths in prod code
}

class FunctionalCoreTest {
   private final FunctionalCore target = new FunctionalCore();

   @Test
   void noMocks() {
      A a = new A("ax");
      B b = new B("bx");

      C c = target.pureFunction(a, b);

      assertThat(c.getData()).isEqualTo("computed with axbx");
   }
}