package victor.testing.spring.hack;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatefulSingletonClient {
   private final StatefulSingleton statefulSingleton;

   public int method() {
      System.out.println("Talking to " + statefulSingleton);
      return statefulSingleton.getAndInc();
   }

}
