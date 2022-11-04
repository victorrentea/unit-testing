package victor.testing.reactor;

import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

public class ReactiveBugs {
    record A(int id) {}
    static class B {}
    static class C {}

    interface Dependency {
        Mono<A> fetchA(int id);
        Mono<B> fetchB(A a);
        Mono<C> fetchC(A a, B b);
        Mono<Void> auditA(A a); // only side-effects, nothing to return
        Mono<Void> saveA(A a);
    }

    private final Dependency dependency;

    public ReactiveBugs(Dependency dependency) {
        this.dependency = dependency;
    }

    // ================================================================
    /**
     * // imperative style code
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
        // TODO how to fix the bug [you] (avoid subscribing twice to monoA)
        // DON'T REUSE THE MONO A
        return dependency
                .fetchA(id)
                .zipWhen(dependency::fetchB)
                .flatMap(TupleUtils.function(dependency::fetchC));
    }

    // ================================================================

    /**
     * Retrieve A, then B for that A (⚠️ you might find no B for that A).
     * Apply logic and save A back to the datastore.
     */
    public Mono<Void> flatMapLoss(int id, String data) {
        return dependency.fetchA(id)
                .zipWhen(a -> dependency.fetchB(a))
                .map(TupleUtils.function((a, b) -> logic(a, b, data)))
                .flatMap(a -> dependency.saveA(a));
    }

    private A logic(A a, B b, String data) {
        // complex logic, implem in imperative style (no Publishers) ❤️
        // returning the results in A. using B if any
        return a;
    }


    // ================================================================

    /**
     * Fire and forget: audit any returned A
     * Default go-to: .flatMap
     * Better solution: .delayUntil
     * Later CR#1: errors in audit should not fail the main flow (eg. errors in Kafka.send)
     * Later CR#2: the main flow should not wait for the audit to be performed (eg. delays in Kafka.send)
     */
    public Mono<A> fireAndForget(int id) {
        return dependency.fetchA(id)
                .flatMap(a -> dependency.auditA(a).thenReturn(a));
    }
}
