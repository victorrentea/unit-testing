package victor.testing.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;
import reactor.test.publisher.TestPublisher;

import java.time.Duration;

import static java.time.Duration.of;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ReactiveFlowTest {
    @Mock
    ReactiveClient client;
    @InjectMocks
    ReactiveFlow target;

    @Test
    void enrichData() {
//        PublisherProbe<StockAmount> stockProbe = PublisherProbe.of(Mono.just(new StockAmount(10))); // TODO simplify
//        when(client.fetchStock(11L)).thenReturn(stockProbe.mono());
//        PublisherProbe<ProductDetails> detailsProbe = PublisherProbe.of(Mono.just(new ProductDetails("desc"))); // TODO simplify
//        when(client.fetchProductDetails(11L)).thenReturn(detailsProbe.mono());

        when(client.fetchStock(11L)).thenReturn(Mono.just(new StockAmount(10)));
        when(client.fetchProductDetails(11L)).thenReturn(Mono.just(new ProductDetails("desc")));



//        // option 1 (imperative style testing)
//        assertThatThrownBy(() -> target.enrichData(11L).block())
//                .isInstanceOf(RuntimeException.class);
//        ProductDto dto = target.enrichData(11L).block();
//        assertThat(dto).isEqualTo(new ProductDto(new ProductDetails("desc"), new StockAmount(10)));

        PublisherProbe<Void> monoVoid = PublisherProbe.of(Mono.empty());
        when(client.auditRequestedProduct(11L)).thenReturn(monoVoid.mono());

//        assertThat(dto.details()).i
        // option 2 reactive style testin
        Mono.defer(() -> target.enrichData(11L))
                .subscribeOn(Schedulers.parallel())
                .as(StepVerifier::create)
//                .assertNext(dto-> assertThat(dto.details()).isEqualTo())
                .expectNext(new ProductDto(new ProductDetails("desc"), new StockAmount(10)))
                // TODO a) CR ProductDto includes a uuid ==> can't use equals anymore; or b) 12 fields to compare => error message = ugly
                .verifyComplete();
//
        assertThat(monoVoid.subscribeCount()).isEqualTo(1);

//        verify(client).auditRequestedProduct(11L);

        // TODO CR: see #enrichData code() ==> purpose for PublisherProbe

//        stockProbe.assertWasSubscribed();
//        assertThat(stockProbe.subscribeCount()).isEqualTo(1); // ?? safer.
//        detailsProbe.assertWasSubscribed();
    }
    @Test
    void enrichData_error() {
        // TODO how to emit an error to tested code
        // 1) programmatic .error()
        // 2) mock(class, EMITS_ERROR)  -- avoid EMITS_EMPTY!! > you loose specificity: looking at a test, the interactions used are not clear
        // 3) global default @see org.mockito.configuration.MockitoConfiguration
        when(client.fetchStock(11L)).thenReturn(Mono.error(new RuntimeException("failing")));
        when(client.fetchProductDetails(11L)).thenReturn(Mono.just(new ProductDetails("desc")));

//        Mono.defer(() ->   // TODO use this to scan for blocking code with blockhound test listener
                        target.enrichData(11L)
//                .subscribeOn(Schedulers.parallel()) // TODO and this
                .as(StepVerifier::create)
                .verifyError(RuntimeException.class)
//                .verifyErrorMessage("failing")
        ;
        // TODO how to check exceptions?
        // TODO should we audit if we emit an error?

    }

    @Test
//    @Timeout(5) // todo alternatives?
    void enrichData_parallel() {
        when(client.fetchStock(11L)).thenReturn(Mono.defer(()-> Mono.delay(ofSeconds(4)).thenReturn(new StockAmount(10))
//        {
//            log.debug("1");
//            return Mono.just(new StockAmount(10))
//                    .doOnSubscribe(s -> log.info("subscr1"))
//                    .doOnNext(s -> log.info("emit0"))
//                    .delayElement(ofSeconds(4))
//                    .doOnNext(e -> log.info("Emit1"));
//        }
        ));
        when(client.fetchProductDetails(11L)).thenReturn(Mono.defer(()->
                Mono.delay(ofSeconds(3)).thenReturn(new ProductDetails("desc"))));

//        TestPublisher.create().
        StepVerifier.withVirtualTime(() ->target.enrichData(11L))
                .expectSubscription()
                .thenRequest(1)
                .thenAwait(ofSeconds(5))
                .expectNextCount(1)
                .verifyComplete();
        // 1) @Timeout (junit)
        // 2) duration = .. .verifyComplete
        // 3) withVirtualTime + all .delayXxx calls in lambdas (executed later in context of virtual time) => see other method in ReactiveFlow
           // ==> the need to emit items at arbitrary rate
    }
}