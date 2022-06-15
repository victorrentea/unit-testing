package victor.testing.tools;

public class TestcontainersUtils {
    public static String injectP6SPYInJdbcUrl(String originalJdbcUrl) {
       String remainingUrl = originalJdbcUrl.substring("jdbc:".length());
       String p6spyUrl = "jdbc:p6spy:" + remainingUrl;
       System.out.println("Injected p6spy into jdbc url:" + p6spyUrl + "; ORIGINAL=" + originalJdbcUrl);
       return p6spyUrl;
    }
}
