package victor.testing.reactor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;
import victor.testing.reactor.ReactiveBugs.A;
import victor.testing.reactor.ReactiveBugs.B;
import victor.testing.reactor.ReactiveBugs.C;
import victor.testing.reactor.ReactiveBugs.Dependency;
import victor.testing.tools.CaptureSystemOutput;

import java.time.Duration;

import static java.time.Duration.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static reactor.test.publisher.PublisherProbe.empty;
import static reactor.test.publisher.PublisherProbe.of;
import static victor.testing.reactor.ReactiveBugs.*;

@ExtendWith(MockitoExtension.class)
class ReactiveBugsTest {
  public static final int ID = 1;
  public static final A A = new A(ID);
  public static final B B = new B();
  public static final C C = new C();
  @Mock
  Dependency dependencyMock;
  @InjectMocks
  ReactiveBugs target;

  @RegisterExtension
  ProbeExtension probes = new ProbeExtension();

  @Test
  void triangleOfDeath() {
    PublisherProbe<A> probeA = of(Mono.just(A));
    when(dependencyMock.fetchA(ID)).thenReturn(probeA.mono());
    when(dependencyMock.fetchB(A)).thenReturn(Mono.just(B));
    when(dependencyMock.fetchC(A, B)).thenReturn(Mono.just(C));

    // when:
    C actual = target.triangleOfDeath(ID).block();

    assertThat(actual).isEqualTo(C);
    assertThat(probeA.subscribeCount()).isEqualTo(1);
  }

  @Test
  void triangleOfDeath_withExtension() {
    when(dependencyMock.fetchA(ID)).thenReturn(probes.subscribeOnce(Mono.just(A)));
    when(dependencyMock.fetchB(A)).thenReturn(Mono.just(B));
    when(dependencyMock.fetchC(A, B)).thenReturn(Mono.just(C));

    // Call production Option #2: (uniform) StepVerifier
    //  usefull when dealing with Fluxes, backpressure, or testing operators for resilience (eg retry, timeout...)
    target.triangleOfDeath(ID)
        .as(StepVerifier::create)
        .expectNext(C)
        .verifyComplete();
  }










  @Test
  void flatMapLoss() {
    A a = new A(1);
    when(dependencyMock.fetchA(1)).thenReturn(Mono.just(a));
    when(dependencyMock.fetchB(a)).thenReturn(Mono.just(new B()));
    when(dependencyMock.saveA(a)).thenReturn(Mono.empty());

    target.flatMapLoss(1, "data").block();
  }

  @Test
  void flatMapLoss_shouldSaveEvenIfNoBIsRetrieved() {
    A a = new A(1);
    when(dependencyMock.fetchA(1)).thenReturn(Mono.just(a));
    when(dependencyMock.fetchB(a)).thenReturn(Mono.empty());
    when(dependencyMock.saveA(a)).thenReturn(Mono.empty());

    target.flatMapLoss(1, "data").block();
  }

//    @Test
//    void flatMapLossWhenNoBFound() {
//        when(dependencyMock.fetchA(ID)).thenReturn(probes.subscribeOnce(Mono.just(A)));
//        when(dependencyMock.fetchB(A)).thenReturn(Mono.empty());
//        PublisherProbe<Void> saveProbe = empty();
//        when(dependencyMock.saveA(A)).thenReturn(probes.subscribeOnce(saveProbe.mono()));
//
//        target.flatMapLoss(ID, "data").block();
//
//        assertThat(saveProbe.subscribeCount()).isEqualTo(1);
//    }

  // ======================================================

  @Test
  void faf() {
    A a = new A(1);
    when(dependencyMock.fetchA(1)).thenReturn(Mono.just(a));

    target.fireAndForget(1).block();

    verify(dependencyMock).auditA(a);
  }







  @Test
  void fire() {
    when(dependencyMock.fetchA(ID)).thenReturn(probes.subscribeOnce(Mono.just(A)));
    when(dependencyMock.auditA(A)).thenReturn(probes.subscribeOnce(Mono.empty()));

    assertThat(target.fireAndForget(ID).block()).isEqualTo(A);
  }

  @Test
  @CaptureSystemOutput
  void flowShouldNotFailWhenAuditErrors(CaptureSystemOutput.OutputCapture capture) {
    when(dependencyMock.fetchA(ID)).thenReturn(probes.subscribeOnce(Mono.just(A)));
    when(dependencyMock.auditA(A)).thenReturn(probes.subscribeOnce(Mono.error(new RuntimeException("TESTEX"))));

    assertThat(target.fireAndForget(ID).block()).isEqualTo(A);
    assertThat(capture.toString()).contains("TESTEX");
  }

  @Test
  void flowShouldNotWaitForAudit() {
    when(dependencyMock.fetchA(ID)).thenReturn(probes.subscribeOnce(Mono.just(A)));
    when(dependencyMock.auditA(A)).thenReturn(probes.subscribeOnce(Mono.delay(ofMillis(500)).then()));

    Duration took = target.fireAndForget(ID)
        .as(StepVerifier::create)
        .expectNext(A)
        .verifyComplete();

    assertThat(took).isLessThan(ofMillis(499));
  }

  @Test
  void flowShouldNotWaitForAudit_virtualTime() {
    when(dependencyMock.fetchA(ID)).thenReturn(probes.subscribeOnce(Mono.just(A)));
    when(dependencyMock.auditA(A)).thenReturn(Mono.delay(ofMinutes(1)).then());

    StepVerifier.withVirtualTime(
            () -> target.fireAndForget(ID).timeout(ofSeconds(30))
        )
        .thenAwait(ofSeconds(40)) // advance the virtual time explicitly
        .expectNext(A)
        .thenAwait(ofSeconds(30)) // advance the virtual time explicitly
        .verifyComplete();
  }
}