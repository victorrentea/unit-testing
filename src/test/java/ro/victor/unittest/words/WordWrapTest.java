package ro.victor.unittest.words;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

@RunWith(Parameterized.class)
public class WordWrapTest {
    private final String input;
    private final int length;
    private final String expectedOutput;

    public WordWrapTest(String input, int length, String expectedOutput) {
        this.input = input;
        this.length = length;
        this.expectedOutput = expectedOutput;
    }

    @Parameterized.Parameters(name = "{0} | {1} | {2}")
    public static List<Object[]> getParameters() {
        return Arrays.asList(
                new Object[]{"a", 1, "a"},
                new Object[]{"a b", 1, "a*b"},
                new Object[]{"a b", 2, "a*b"},
                new Object[]{"a b", 3, "a b"},
                new Object[]{"a a b", 3, "a a*b"},
                new Object[]{"a aa", 3, "a*aa"},
                new Object[]{"aa", 1, "aa"},
                new Object[]{"a a a", 1, "a*a*a"},
                new Object[]{"aa aa", 1, "aa*aa"},
                new Object[]{"aa aa a a", 3, "aa*aa*a a"},
                new Object[]{"aa  aa a a", 3, "aa*aa*a a"},
                new Object[]{"  aa    aa   a a  ", 3, "aa*aa*a a"},
                new Object[]{"  aa    aa   a a  ", 4, "aa*aa*a a"},
                new Object[]{"a ", 1, "a"}
        );
    }

    @Test
    public void test() {
        assertThat(f(input, length)).isEqualTo(expectedOutput);
    }


    private String f(String s, int length) {
        String trimmed = s.trim();

        if (trimmed.length() <= length) {
            return trimmed;
        }
        int breakPosition = length;
        while (breakPosition >= 0 && trimmed.charAt(breakPosition) != ' ') {
            breakPosition--;
        }
        while (breakPosition >= 1 && trimmed.charAt(breakPosition - 1) == ' ') {
            breakPosition--;
        }
        if (breakPosition < 0) {
            breakPosition = trimmed.indexOf(' ');
            if (breakPosition < 0) {
                return trimmed;
            }
        }
        return trimmed.substring(0, breakPosition) + "*" + f(trimmed.substring(breakPosition + 1), length);
    }
}
