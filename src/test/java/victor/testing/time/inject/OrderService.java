package victor.testing.time.inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class OrderService {
    @Autowired
    private Clock clock;

    public LocalDateTime endOfBreak() {
        LocalDateTime now = LocalDateTime.now(clock);
        return now.plusMinutes(10);
    }


}
