package victor.testing.prework;

import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AsaNu {
    @Test
    void trebuie_sters_acest_test_intrucat_este_inutil() {
        CuDate x = new CuDate();

        x.setName("a");

        assertThat(x.getName()).isEqualTo("a");
    }
}

@Data
class CuDate {
    private String name;
}
