package victor.testing.reactive;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.verify;
import static reactor.core.publisher.Mono.when;


@ExtendWith(MockitoExtension.class)
public class ReactiveMockito {
   private static final Answer<Object> EMITS_ERROR = i -> Mono.error(new RuntimeException("ERROR emitted by default"));
   @Mock
   Dependency dependency;
   @InjectMocks
   TestedCode testedCode;

   @Test
   void noLaunchInPeace() {
      testedCode.peace().block();

      Mockito.verifyNoMoreInteractions(dependency);
   }
   @Test
   void launchInWar() {
//      when(dependency.launchMissile()).

      testedCode.war().block();

      verify(dependency).launchMissile();
   }
}

class Dependency {
   public Mono<Void> launchMissile() {
      return Mono.fromRunnable(() -> System.out.println("BUUM"));
   }
}

class TestedCode {
   private final Dependency dependency;

   TestedCode(Dependency dependency) {
      this.dependency = dependency;
   }

   public Mono<Void> peace() {
      return Mono.empty();
   }
   public Mono<Void> war() {
      return dependency.launchMissile();
   }
}