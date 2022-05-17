package victor.testing.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;

public class ReactiveFlow {
    private final ReactiveClient client;

    public ReactiveFlow(ReactiveClient client) {
        this.client = client;
    }

    public Mono<ProductDto> enrichData(Long productId) {
        return client.fetchProductDetails(productId)
                .zipWith(client.fetchStock(productId), ProductDto::new);
    }

    @PostConstruct
    public void onstartup() {
        Flux<Long> kafkaMessages = Flux.empty(); // imagine dragons
        subscribeToKafkaStream(kafkaMessages);
    }

    public void subscribeToKafkaStream(Flux<Long> productIdFlux) {
        productIdFlux.bufferTimeout(3, Duration.ofSeconds(2))
                .flatMap(pageOfMessages -> client.fetchProductDetailsInPages(pageOfMessages))
//                .flatMap(repo::save)
                .subscribe();
    }
}


class ReactiveClient {
    public Mono<StockAmount> fetchStock(long productId) {
//        WebClient...
        return Mono.empty();
    }
    public Mono<ProductDetails> fetchProductDetails(long productId) {
        return Mono.empty();
    }

    public Flux<ProductDetails> fetchProductDetailsInPages(List<Long> productIds) {
        return Flux.empty();
    }
}

record StockAmount(int value) {
}
record ProductDetails(String description) {
}
record ProductDto(ProductDetails details, StockAmount stock/*, UUID uniqueResponseId*/){}
