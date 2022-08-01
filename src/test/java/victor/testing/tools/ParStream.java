package victor.testing.tools;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ParStream {
    public static void main(String[] args) {

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        log.info("pe main");
        List<Integer> list = numbers.parallelStream()
                .map(n -> {
                    // REST sau SQL
                    log.info("Ruleaza pe un thread pool dedicat existent in oeice JVM : NCPU-1 threaduri");
                    return n;
                }).collect(Collectors.toList());
    }
}
