package victor.testing.tdd.unusualspending;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EmailSender {
    private final  EmailClient emailClient;

    public EmailSender(EmailClient emailClient) {
        this.emailClient = emailClient;
    }

    public Mono<Void> sendUnusualSpendingEmail(UnusualSpendingReport unusualSpendingReport, String userId) {
        String subject = "Unusual spending of $%d detected!".formatted(unusualSpendingReport.total());
        String body = "Hello card user!\n" +
                "\n" +
                "We have detected unusually high spending on your card in these categories:\n" +
                "\n" +
                unusualSpendingReport.amountPerCategory().entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .map(e -> "* You spent $%d on %s\n".formatted(e.getValue(), e.getKey().name().toLowerCase()))
                        .collect(Collectors.joining())
                +"\n" +
                "Love,\n" +
                "\n" +
                "The Credit Card Company\n";
        return emailClient.sendEmail(userId, subject, body);

    }
}
