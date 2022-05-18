package victor.testing.tdd.unusualspending;

import java.time.LocalDate;

public record Payment(int amount, Category category, LocalDate date) {
}
