package ro.victor.unittest.tdd.outsidein;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Clock {
    public String getDateAsString() {
        // now.format(....)
        return today().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    protected LocalDate today() {
        return LocalDate.now();
    }
}
