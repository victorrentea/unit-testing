package victor.testing.tdd.unusualspending;


import java.time.LocalDate;

public record PaymentDto(int amount,
                         String description,
                         Category category,
                         LocalDate date) {
}
