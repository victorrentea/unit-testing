package victor.testing.tools;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestcontainersUtils {
    public static String injectP6SPYInJdbcUrl(String originalJdbcUrl) {
       String remainingUrl = originalJdbcUrl.substring("jdbc:".length());
       String p6spyUrl = "jdbc:p6spy:" + remainingUrl;
       log.debug("Injected p6spy into jdbc url= " + p6spyUrl + "  (orignal= " + originalJdbcUrl+")");
       return p6spyUrl;
    }
}
