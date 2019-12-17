package ro.victor.unittest;

import ch.qos.logback.core.helpers.CyclicBuffer;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ImpossibleBoundaryTest {

    private final Prod prod = new Prod();

    @Test
    public void sumok() {
        assertThat(prod.sum(1,1)).isEqualTo(2);
    }
    @Test
    public void sumokCuNull() {
        assertThat(prod.sum(null,1)).isEqualTo(1);
    }
}


class Prod {

    int sum(Integer a, Integer b) {
        if (a == null) {
            return b;
        }
        return a + b;
    }
}

class CodClient {
    Prod prod = new Prod();

    public void m() {
        String[] arr = {"a","b"};
        prod.sum(2, arr.length);

    }
}