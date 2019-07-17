package ro.victor.unittest.tdd.outsidein;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;


public class ClockShould {
    @Test
    public void return_formatted_date_as_dd_MM_yyyy() {
        Clock clock = new Clock() {
            @Override
            protected LocalDate today() {
                return LocalDate.of(2019,1,1);
            }
        };
        assertEquals("01/01/2019", clock.getDateAsString());
    }
}
