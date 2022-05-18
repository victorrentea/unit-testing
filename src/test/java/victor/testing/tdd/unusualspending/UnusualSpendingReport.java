package victor.testing.tdd.unusualspending;

import java.util.Map;

public record UnusualSpendingReport(int total, Map<Category, Integer> amountPerCategory) {
}
