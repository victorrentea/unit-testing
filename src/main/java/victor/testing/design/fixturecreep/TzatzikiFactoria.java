package victor.testing.design.fixturecreep;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TzatzikiFactoria {
   private final Dependency dependency;
   public void makeTzatziki() {
      if (!dependency.isYogurt()) {
         throw new IllegalArgumentException();
      }
      // complex logic: 5 ifs
   }
}
