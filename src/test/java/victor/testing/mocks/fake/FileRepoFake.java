package victor.testing.mocks.fake;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Primary
@Profile("prod")
@Component
public class FileRepoFake/* implements IFileRepo*/{
   @PostConstruct
   public void method() {
      throw new IllegalArgumentException("Eu plec");
   }
}
