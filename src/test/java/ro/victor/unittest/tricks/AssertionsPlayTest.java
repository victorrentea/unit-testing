package ro.victor.unittest.tricks;

import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class AssertionsPlayTest {

    @BeforeClass

    @Test
    public void m() {
        String score = "Game won Player1";
        Assertions.assertThat(score).isEqualTo("Game won Player1");
        List<String> list = Arrays.asList("a", "b");
        Assertions.assertThat(list).contains("b", "a");
        List<Point> list2 = Arrays.asList(new Point(1, 2));
        Assertions.assertThat(list2).anyMatch(e -> e.getX() == 1);

    }
}
