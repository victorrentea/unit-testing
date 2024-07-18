package victor.testing.groceries;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class GroceriesMessageHandler {
  private final GroceryRepo groceryRepo;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void handleRequest(GroceriesRequestMessage requestMessage) {
    int total = 0;
    for (GroceryRequest groceryRequest : requestMessage.groceries()) {
      Optional<Grocery> grocery = groceryRepo.findByName(groceryRequest.grocery());
      if (grocery.isEmpty()) {
        kafkaTemplate.send("grocery-not-found", new GroceryNotFoundEvent(requestMessage.id(), groceryRequest.grocery()));
      } else {
        total += grocery.get().getPrice() * groceryRequest.count();
      }
    }
    kafkaTemplate.send("grocery-response", new GroceriesReplyMessage(requestMessage.id(), total));
  }

  public record GroceryNotFoundEvent(String requestId, String grocery) {
  }

  public record GroceriesReplyMessage(String requestId, int totalPrice) {
  }

  public record GroceriesRequestMessage(String id, List<GroceryRequest> groceries) {
  }

  public record GroceryRequest(String grocery, int count) {
  }
}
