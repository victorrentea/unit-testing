package victor.testing.approval.message;

import java.util.List;

public class GroceriesResponseMessage {
    private final String requestId;
    private final int totalPrice;

    public GroceriesResponseMessage(String requestId, int totalPrice) {
        this.requestId = requestId;
        this.totalPrice = totalPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getRequestId() {
        return requestId;
    }
}
