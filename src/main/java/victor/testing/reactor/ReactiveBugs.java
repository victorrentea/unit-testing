package victor.testing.reactor;

import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

import java.util.Optional;

public class ReactiveBugs {
    private static final Logger log = LoggerFactory.getLogger(ReactiveBugs.class);
    record A(int id) {}
    static class B {}
    static class C {}

    interface Dependency {
        Mono<A> fetchA(int id);
        Mono<B> fetchB(A a);
        Mono<C> fetchC(A a, B b);
        Mono<Void> auditA(A a); // only side effects, nothing to return
        Mono<Void> saveA(A a);
    }

    private final Dependency dependency;

    public ReactiveBugs(Dependency dependency) {
        this.dependency = dependency;
    }

    // ================================================================
    /**
     * Do this: (imperative equivalent)
     * a = fetchA(id);
     * b = fetchB(a);
     * c = fetchC(a, b);
     *
     * <p>
     * a --> b
     * ↓   /
     * c <-
     */
    public Mono<C> triangleOfDeath(int id) {
        // TODO write the test; did we miss anything?
        Mono<A> monoA = dependency.fetchA(id);
        Mono<B> monoB = monoA.flatMap(a -> dependency.fetchB(a));
        Mono<C> monoC = monoA.zipWith(monoB)
                .flatMap(t2 -> dependency.fetchC(t2.getT1(), t2.getT2()));
        return monoC;
    }

    // ================================================================

    /**
     * Retrieve A, then B for that A
     * (⚠️ you might find no B for that A).
     * Apply logic and save A back to the datastore.
     */
    public Mono<Void> flatMapLoss(int id, String data) {
        return dependency.fetchA(id)
                .zipWhen(a -> dependency.fetchB(a)
                        .map(Optional::of)
                        .defaultIfEmpty(Optional.empty())) // never emits any value if fetchB returns empty()
//                .zipWhen(a -> {
//                    try {
//                        return dependency.fetchB(a).defaultIfEmpty(null);
//                    } catch (Exception e) {
//                        log.error("YES!");
//                        throw new RuntimeException(e);
//                    }
//                }) // never emits any value if fetchB returns empty()
                .map(TupleUtils.function((a, b) -> logic(a, b, data)))
                .flatMap(a -> dependency.saveA(a));
    }

    @VisibleForTesting
     A logic(A a, Optional<B> b, String data) {
        if (b.isPresent()) {
            // stuff with B
        }
        // complex logic, implem in imperative style (no Publishers) ❤️
        // returning the results in A. using B if any
        return a;
    }


    // ================================================================

    /**
     * *** Fire and forget **
     * Original requirement: Audit any returned A
     * Default go-to: .flatMap
     * Better solution: .delayUntil
     * Later CR#1: errors in audit should not fail the main flow (eg. errors in Kafka.send)
     *      please make sure any errors in audit do not kill the flow no .error
     *
     * Later
     * Problem: eg. delays in Kafka.send
     * Req: the main flow should not wait for the audit to complete = "fire-and-forget"
     */
    public Mono<A> fireAndForget(int id) {
        return dependency.fetchA(id)
                .doOnNext(a -> dependency.auditA(a)
                            .subscribe(v->{}, e -> log.error("OMG!", e) ))

//                .delayUntil(a -> dependency.auditA(a)
//                        .doOnError(e -> log.error("OMG!", e))
//                        .onErrorResume(e -> Mono.empty())) // identical beh with the line below
//                .flatMap(a -> dependency.auditA(a).thenReturn(a))

                // 95% of time we uyse doOnNext for logging: burry the logging deeper in the 'colabborator methods',
                // don't pollute your topLevel reactive chain (push it inside fetchA()

//                .doOnNext(a -> dependency.auditA(a).block()) // just the blocking(worst) alternative to flatMap/delayUntil
                ;
    }
}
