package victor.testing.approval.message;

import lombok.Value;

@Value
public class GroceryNotFoundEvent {
    private final String requestId;
    private final String grocery;

    public GroceryNotFoundEvent(String requestId, String grocery) {
        this.requestId = requestId;
        this.grocery = grocery;
    }

    public String getGrocery() {
        return grocery;
    }

    public String getRequestId() {
        return requestId;
    }
}
