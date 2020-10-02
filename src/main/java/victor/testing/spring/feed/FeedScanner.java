package victor.testing.spring.feed;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class FeedScanner {
   @Transactional // 2 Assume some Spring juice around
   public void removeComments(List<String> lines) {
      // 1 Assume smart logic here, further dependencies

   }
}
