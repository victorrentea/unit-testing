package victor.testing.designhints.functionalcore;

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
   void useCase7() {
      A a = new A();
      B b = new B();
      when(repoA.findById(1L)).thenReturn(a);
      when(repoB.findById(2L)).thenReturn(b);

      target.useCase7(1L, 2L);

      verify(repoC).save(cCaptor.capture());
      assertThat(cCaptor.getValue().getData()).isEqualTo("via complex logic");
   }
}