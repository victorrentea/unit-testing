package victor.testing.approval.message;

import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class GroceriesMessageHandler {
    private final GroceryRepo groceryRepo;
    private final KafkaSender kafkaSender;

    public GroceriesMessageHandler(GroceryRepo groceryRepo, KafkaSender kafkaSender) {
        this.groceryRepo = groceryRepo;
        this.kafkaSender = kafkaSender;
    }

    public void handleRequest(GroceriesRequestMessage requestMessage) {
        int total = 0;
        for (GroceryRequest groceryRequest : requestMessage.getGroceries()) {
            Optional<Grocery> grocery = groceryRepo.findByName(groceryRequest.getGrocery());
            if (grocery.isEmpty()) {
                kafkaSender.send("grocery-not-found", new GroceryNotFoundEvent(requestMessage.getId(), groceryRequest.getGrocery()));
            } else {
                total += grocery.get().getPrice() * groceryRequest.getCount();
            }
        }
        kafkaSender.send("grocery-response", new GroceriesResponseMessage(requestMessage.getId(), total));
    }
}
