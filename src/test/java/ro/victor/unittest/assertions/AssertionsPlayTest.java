package ro.victor.unittest.assertions;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class AssertionsPlayTest {

    @Test
    public void m() {
        String score = "Game won Player1";
        assertThat(score).isEqualTo("Game won Player1");
        List<String> list = asList("a", "b");
        assertThat(list).contains("b", "a");
        List<Point> list2 = asList(new Point(1, 2));
        assertThat(list2).anyMatch(e -> e.getX() == 1);

    }
}
