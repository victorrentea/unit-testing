package victor.testing.reactive;

import reactor.core.publisher.Mono;

import java.util.UUID;

public class ReactiveFlow {
    private final ReactiveClient client;

    public ReactiveFlow(ReactiveClient client) {
        this.client = client;
    }

    public Mono<ProductDto> enrichData(Long productId) {
        return client.fetchProductDetails(productId)
                .zipWith(client.fetchStock(productId), ProductDto::new);
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
}

record StockAmount(int value) {
}
record ProductDetails(String description) {
}
record ProductDto(ProductDetails details, StockAmount stock/*, UUID uniqueResponseId*/){}
