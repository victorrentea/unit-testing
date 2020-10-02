package victor.testing.spring.feed;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class FeedScanner {
   @Transactional // 2 Assume some Spring juice around eg cacheable
   public void removeComments(List<String> lines) {
      lines.removeIf(line -> line.startsWith("#"));
   }
}
