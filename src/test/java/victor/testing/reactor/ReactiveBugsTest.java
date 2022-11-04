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
import victor.testing.tools.ProbeExtension;

import java.time.Duration;

import static java.time.Duration.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static reactor.test.publisher.PublisherProbe.empty;
import static reactor.test.publisher.PublisherProbe.of;

@ExtendWith(MockitoExtension.class)
class ReactiveBugsTest {
    public static final int ID = 1;
    public static final A A = new A(ID);
    public static final ReactiveBugs.B B = new B();
    public static final ReactiveBugs.C C = new C();
    @Mock
    Dependency dependencyMock;
    @InjectMocks
    ReactiveBugs target;

    @RegisterExtension
    ProbeExtension probes = new ProbeExtension();

    @Test
    void triangleOfDeath() {
        PublisherProbe<A> monoA = of(Mono.just(A));
        when(dependencyMock.fetchA(ID)).thenReturn(monoA.mono());
        when(dependencyMock.fetchB(A)).thenReturn(Mono.just(B));
        when(dependencyMock.fetchC(A, B)).thenReturn(Mono.just(C));

        // pragmatic but use the EVIL B... word
//        C actual = target.triangleOfDeath(ID).block(); // subscibed and BLOCKed the JUnit main thread for the called f to complete
//        assertThat(actual).isEqualTo(C);


//        StepVerifier.create(target.triangleOfDeath(ID)); // neah...

        target.triangleOfDeath(ID)
                .as(StepVerifier::create)
                .expectNext(C)
                .verifyComplete();

        assertThat(monoA.subscribeCount()).isEqualTo(1);
    }

    @Test
    void flatMapLoss() {
        when(dependencyMock.fetchA(ID)).thenReturn(Mono.just(A));
        when(dependencyMock.fetchB(A)).thenReturn(Mono.just(B));
        PublisherProbe<Void> saveProbe = empty();
        when(dependencyMock.saveA(A)).thenReturn(saveProbe.mono());

        target.flatMapLoss(ID, "data")
                .as(StepVerifier::create)
                .verifyComplete(); // nothing happens in prd until you subscribe

        assertThat(saveProbe.subscribeCount()).isEqualTo(1);
    }

    @Test
    void flatMapLossWhenNoBFound() {
        when(dependencyMock.fetchA(ID)).thenReturn(probes.once(Mono.just(A)));
        when(dependencyMock.fetchB(A)).thenReturn(Mono.empty());
        PublisherProbe<Void> saveProbe = empty();
        when(dependencyMock.saveA(A)).thenReturn(saveProbe.mono());

        target.flatMapLoss(ID, "data")
                .as(StepVerifier::create)
                .verifyComplete(); // nothing happens in prd until you subscribe

        assertThat(saveProbe.subscribeCount()).isEqualTo(1);
    }

    @Test
    void fire1() {
        when(dependencyMock.fetchA(ID)).thenReturn(probes.once(Mono.just(A)));
        when(dependencyMock.auditA(A)).thenReturn(probes.once(Mono.empty()));

        assertThat(target.fireAndForget(ID).block()).isEqualTo(A);
    }
    @Test
    @CaptureSystemOutput
    void flowShouldNotFailWhenAuditErrors(CaptureSystemOutput.OutputCapture capture) {
        when(dependencyMock.fetchA(ID)).thenReturn(probes.once(Mono.just(A)));
        when(dependencyMock.auditA(A)).thenReturn(probes.once(Mono.error(new RuntimeException("TESTEX"))));

        assertThat(target.fireAndForget(ID).block()).isEqualTo(A);
        assertThat(capture.toString()).contains("TESTEX");
    }

    @Test
    void flowShouldNotWaitForAudit() {
        when(dependencyMock.fetchA(ID)).thenReturn(probes.once(Mono.just(A)));
        when(dependencyMock.auditA(A)).thenReturn(probes.once(Mono.delay(ofMillis(500)).then()));

        Duration took = target.fireAndForget(ID)
                .as(StepVerifier::create)
                .expectNext(A)
                .verifyComplete();

        assertThat(took).isLessThan(ofMillis(499));
    }
    @Test
    void flowShouldNotWaitForAudit_virtualTime() {
        when(dependencyMock.fetchA(ID)).thenReturn(probes.once(Mono.just(A)));
//        when(dependencyMock.auditA(A)).thenReturn(Mono.delay(ofMinutes(1)) // NOW delay of 1 minute really plays 1 min
//                .doOnTerminate(() -> System.out.println("AUDIT DOWN")).then());

        //
        when(dependencyMock.auditA(A)).thenAnswer(x -> Mono.delay(ofMinutes(1)) // delay uses the virtual time
                .doOnTerminate(() -> System.out.println("AUDIT DOWN")).then());

//        Duration duration = StepVerifier.create(target.fireAndForget(ID))
        Duration duration = StepVerifier.withVirtualTime(
                        // just before    calling this lambda, the time is made "virtual" on this thread
                        () -> target.fireAndForget(ID).timeout(ofSeconds(30))
                        // time is restored
                )
                .thenAwait(ofSeconds(40)) // advance the virtual time explicitly
                .expectNext(A)
                .thenAwait(ofSeconds(30)) // advance the virtual time explicitly
                .verifyComplete();

        assertThat(duration).isLessThan(ofMillis(499));
    }
}