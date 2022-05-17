package victor.testing.reactive;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

import java.time.Duration;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReactiveFlowTest {
    @Mock
    ReactiveClient client;
    @InjectMocks
    ReactiveFlow target;

    @Test
    void enrichData() {
        PublisherProbe<StockAmount> stockProbe = PublisherProbe.of(Mono.just(new StockAmount(10))); // TODO simplify
        when(client.fetchStock(11L)).thenReturn(stockProbe.mono());
        PublisherProbe<ProductDetails> detailsProbe = PublisherProbe.of(Mono.just(new ProductDetails("desc"))); // TODO simplify
        when(client.fetchProductDetails(11L)).thenReturn(detailsProbe.mono());

        target.enrichData(11L)
                .as(StepVerifier::create)
                .expectNext(new ProductDto(new ProductDetails("desc"), new StockAmount(10)))
                // TODO a) CR ProductDto includes a uuid ==> can't use equals anymore; or b) 12 fields to compare => error message = ugly
                .verifyComplete();

        stockProbe.assertWasSubscribed();
        detailsProbe.assertWasSubscribed();
    }
    @Test
    void enrichData_error() {
        // TODO try to comment next line => @see org.mockito.configuration.MockitoConfiguration
        when(client.fetchStock(11L)).thenReturn(Mono.error(new RuntimeException("failing")));
        when(client.fetchProductDetails(11L)).thenReturn(Mono.just(new ProductDetails("desc")));

        target.enrichData(11L)
                .as(StepVerifier::create)
                .verifyError(RuntimeException.class)
//                .verifyErrorMessage("failing")
        ;
        // todo debate: alternative way to check exception?
    }

    @Test
    @Timeout(5) // todo alternatives?
    void enrichData_parallel() {
        when(client.fetchStock(11L)).thenReturn(
                Mono.just(new StockAmount(10)).delayElement(ofSeconds(4)));
        when(client.fetchProductDetails(11L)).thenReturn(
                Mono.just(new ProductDetails("desc")).delayElement(ofSeconds(3)));

        StepVerifier.withVirtualTime(() ->target.enrichData(11L))
                .thenAwait(ofSeconds(5))
                .expectNextCount(1)
                .verifyComplete();
        // 1) @Timeout (junit)
        // 2) duration = .. .verifyComplete
        // 3) withVirtualTime + all .delayXxx calls in lambdas (executed later in context of virtual time) => see other method in ReactiveFlow
    }
}

// fluxul 2: vin mesaje pe kafka. le grupam cate 3 in pagini cu max 100 ms delay -> trimitem la altii