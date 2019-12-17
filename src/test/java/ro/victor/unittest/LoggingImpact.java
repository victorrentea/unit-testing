package ro.victor.unittest;

import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class LoggingImpact {
    public static void main(String[] args) {
        List<String> strings = Arrays.asList("a", "a", "a", "a", "a", "a", "a", "a");


        if (log.isDebugEnabled()) {
            log.debug("Lista este {}", Arrays.toString(args));
        }
//        Clock clock = Clock.fixed()
//        LocalDateTime now = LocalDateTime.now(clock);

        log.debug("Lista este  " + strings.toString());
        log.debug("Lista este {}", strings);
    }
}
