package victor.testing.reactor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.publisher.PublisherProbe;
import victor.testing.reactor.ReactiveBugs.A;
import victor.testing.reactor.ReactiveBugs.B;
import victor.testing.reactor.ReactiveBugs.C;
import victor.testing.reactor.ReactiveBugs.Dependency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReactiveBugsTest {
    public static final int ID = 1;
    public static final ReactiveBugs.A A = new A(ID);
    public static final ReactiveBugs.B B = new B();
    public static final ReactiveBugs.C C = new C();
    @Mock
    Dependency dependencyMock;
    @InjectMocks
    ReactiveBugs target;

    @Test
    void triangleOfDeath() {
        // Idea #2 let's mock the Mono itself : which of the 7 subscribe(..) methods you want to verify >
        // NEVER Mock MONO!!
//        Mono mock = mock(Mono.class);
        //        PublisherProbe.of(); // inspect the calls
        when(dependencyMock.fetchA(ID)).thenReturn(Mono.just(A).doOnSubscribe(s -> {
            System.out.println("FIRE IN THE HOLE!");
        }));
        when(dependencyMock.fetchB(A)).thenReturn(Mono.just(B));
        when(dependencyMock.fetchC(A, B)).thenReturn(Mono.just(C));

        Mono<C> monoC = target.triangleOfDeath(ID);

        C actual = monoC.block(); // subscibed and BLOCKed the JUnit main thread for the called f to complete
        assertThat(actual).isEqualTo(C);
        verify(dependencyMock).fetchA(ID);
    }
}