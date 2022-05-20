package victor.testing.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;

public class ReactiveFlow {
    private final ReactiveClient client;

    public ReactiveFlow(ReactiveClient client) {
        this.client = client;
    }

    public Mono<ProductDto> enrichData(Long productId) {
        block_OMG();
        //  TODO CR on each product returned successfully, call client.auditRequestedProduct(productId)
        Mono<ProductDetails> mono = client.fetchProductDetails(productId);

        return mono
                .zipWith(client.fetchStock(productId), ProductDto::new)
                ;
    }

    private void block_OMG() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void onstartup() {
        Flux<Long> kafkaMessages = Flux.empty(); // imagine dragons
        subscribeToKafkaStream(kafkaMessages);
    }

    public void subscribeToKafkaStream(Flux<Long> productIdFlux) {
        productIdFlux
                .flatMap(id -> client.fetchProductDetails(id))
//                .bufferTimeout(3, Duration.ofSeconds(2))
//                .flatMap(pageOfIds -> client.fetchProductDetailsInPages(pageOfIds))
//                .flatMap(repo::save)
                .subscribe();
    }
}


interface ReactiveClient {
    Mono<StockAmount> fetchStock(long productId);

    Mono<ProductDetails> fetchProductDetails(long productId);

    Flux<ProductDetails> fetchProductDetailsInPages(List<Long> productIds);

    Mono<Void> auditRequestedProduct(Long productId);
}

record StockAmount(int value) {
}

record ProductDetails(String description) {
}

record ProductDto(ProductDetails details, StockAmount stock/*, UUID uniqueResponseId*/) {
}
