package victor.testing.approval.message;

public class GroceryRequest {
    private final String grocery;
    private final int count;

    public GroceryRequest(String grocery, int count) {
        this.grocery = grocery;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getGrocery() {
        return grocery;
    }
}
