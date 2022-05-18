package victor.testing.tdd.unusualspending;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.*;

@Service
public class PaymentAnalyzer {
    private final TimeProvider timeProvider;

    public PaymentAnalyzer(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public Optional<UnusualSpendingReport> analyze(List<Payment> payments) {
        int currentMonth = timeProvider.currentLocalDate().getMonthValue();
        Map<Category, Integer> paymentsLastMonth = payments.stream()
                .filter(p -> p.date().getMonthValue() != currentMonth)
                .collect(groupingBy(Payment::category, summingInt(Payment::amount)));
        Map<Category, Integer> paymentsThisMonth = payments.stream()
                .filter(p -> p.date().getMonthValue() == currentMonth)
                .collect(groupingBy(Payment::category, summingInt(Payment::amount)));

        Map<Category, Integer> totalsPerCategory = paymentsThisMonth.entrySet().stream()
                .filter(e -> paymentsLastMonth.containsKey(e.getKey()))
                .filter(e -> e.getValue() > paymentsLastMonth.get(e.getKey()) * 1.5)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

//        Map<Category, Map<Boolean, Integer>> god = payments.stream()
//                .collect(groupingBy(Payment::category,
//                        partitioningBy(p -> p.date().getMonthValue() == currentMonth, summingInt(Payment::amount))));
//        Map<Category, Integer> totalsPerCategory = god.entrySet().stream()
//                .filter(e -> e.getValue().get(true) != null)
//                .filter(e -> e.getValue().get(false) != null)
//                .filter(e -> e.getValue().get(true) > e.getValue().get(false) * 1.5)
//                .collect(toMap(Map.Entry::getKey, e -> e.getValue().get(true)));
        if (totalsPerCategory.isEmpty()) {
            return Optional.empty();
        }
        int grandTotal = totalsPerCategory.values().stream().mapToInt(i -> i).sum();
        return Optional.of(new UnusualSpendingReport(grandTotal, totalsPerCategory));
    }
}
