package victor.testing.tdd.unusualspending;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Payment(int amount, Category category, LocalDate dateTime)  {
}
