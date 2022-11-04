package victor.testing.reactor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;
import victor.testing.reactor.ReactiveBugs.A;
import victor.testing.reactor.ReactiveBugs.B;
import victor.testing.reactor.ReactiveBugs.C;
import victor.testing.reactor.ReactiveBugs.Dependency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
        PublisherProbe<ReactiveBugs.A> aProbe = of(Mono.just(A));
        when(dependencyMock.fetchA(ID)).thenReturn(aProbe.mono());
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
        PublisherProbe<ReactiveBugs.A> aProbe = of(Mono.just(A));
        when(dependencyMock.fetchA(ID)).thenReturn(aProbe.mono());
        when(dependencyMock.fetchB(A)).thenReturn(Mono.empty());
        PublisherProbe<Void> saveProbe = empty();
        when(dependencyMock.saveA(A)).thenReturn(saveProbe.mono());

        target.flatMapLoss(ID, "data")
                .as(StepVerifier::create)
                .verifyComplete(); // nothing happens in prd until you subscribe

        assertThat(saveProbe.subscribeCount()).isEqualTo(1);
    }
}