package victor.testing.tdd.outsidein;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class ClockShould {
    @Spy
    private Clock clock;
    @Test
    public void return_formatted_date_as_dd_MM_yyyy() {
        Mockito.doReturn(LocalDate.of(2019,1,1)).when(clock).today();
        assertThat(clock.getDateAsString()).isEqualTo("01/01/2019");
    }
}
