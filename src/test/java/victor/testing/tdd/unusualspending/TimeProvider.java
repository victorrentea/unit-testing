package victor.testing.tdd.unusualspending;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TimeProvider {
    public LocalDate currentLocalDate() {
        return LocalDate.now();
    }
}
