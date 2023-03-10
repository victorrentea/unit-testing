package victor.testing.design.purity.intro;

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
   @InjectMocks ImperativeShell target;
   @Captor ArgumentCaptor<C> cCaptor;

   @Test
   void case51() {
      A a = new A("ax");
      B b = new B("bx");
      when(repoA.findById(1L)).thenReturn(a);
      when(repoB.findById(2L)).thenReturn(b);

      target.case51(1L, 2L);

      verify(repoC).save(cCaptor.capture());
      assertThat(cCaptor.getValue().getData()).isEqualTo("computed with axbx");
   }
   // TODO write 6 more such tests, to explore other paths in prod code
}