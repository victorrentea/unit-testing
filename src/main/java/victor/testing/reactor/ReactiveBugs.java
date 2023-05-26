package victor.testing.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.shaded.com.google.common.annotations.VisibleForTesting;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

public class ReactiveBugs {
  private static final Logger log = LoggerFactory.getLogger(ReactiveBugs.class);

  record A(int id) {
  }

  static class B {
  }

  static class C {
  }

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
//    Mono<A> monoA = dependency.fetchA(id).cache(); // fix#1, but a bit scary
// fix#2: don't ever keep Mono/Flux in variables

    Mono<A> monoA = dependency.fetchA(id);

    Mono<C> monoC = monoA.flatMap(a -> {

      Mono<B> monoB = dependency.fetchB(a);
      return monoB.flatMap(b -> dependency.fetchC(a, b));
    });

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
        .zipWhen(a -> dependency.fetchB(a)) // never emits any value if fetchB returns empty()
        .map(TupleUtils.function((a, b) -> logic(a, b, data)))
        .flatMap(a -> dependency.saveA(a));
  }

  @VisibleForTesting
  A logic(A a, B b, String data) {
    if (b != null) {
      // stuff with B
      System.out.println("Stuff with " + b);
    }
    // complex domain logic, implemented in imperative style (no Reactive) = ❤️
    return a; // returning the results in A. using B if available
  }


  // ================================================================

  /**
   * *** Fire and forget **
   * Original requirement: Audit any returned A
   * Default go-to: .flatMap
   * Better solution: .delayUntil
   * Later CR#1: errors in audit should not fail the main flow (eg. errors in Kafka.send)
   * please make sure any errors in audit do not kill the flow no .error
   * <p>
   * Later
   * Problem: eg. delays in Kafka.send
   * Req: the main flow should not wait for the audit to complete = "fire-and-forget"
   */
  public Mono<A> fireAndForget(int id) {
    return dependency.fetchA(id)
        .doOnNext(a -> dependency.auditA(a))
        ;
  }
}
