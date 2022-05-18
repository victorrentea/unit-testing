package victor.testing.tdd.unusualspending;

import java.util.Map;

public record UnsualPaymentReport(Map<Category, Integer> unusualSpendingPerCategory) {
    public UnsualPaymentReport {
        assert !unusualSpendingPerCategory.isEmpty();
    }
}
