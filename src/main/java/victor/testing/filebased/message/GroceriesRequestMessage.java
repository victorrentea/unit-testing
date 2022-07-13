package victor.testing.filebased.message;

import lombok.Data;

import java.util.List;

@Data
public class GroceriesRequestMessage {
    private final String id;
    private final List<GroceryRequest> groceries;

    public GroceriesRequestMessage(String id, List<GroceryRequest> groceries) {
        this.id = id;
        this.groceries = groceries;
    }

    public List<GroceryRequest> getGroceries() {
        return groceries;
    }

    public String getId() {
        return id;
    }
}
