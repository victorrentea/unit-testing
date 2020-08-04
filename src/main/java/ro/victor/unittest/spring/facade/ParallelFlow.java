package ro.victor.unittest.spring.facade;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Service
@RequiredArgsConstructor
public class ParallelFlow {
   private final ImageProcessingServiceClient client;


   public void doIt(String image) {
      for (int i = 0; i < 5; i++) {
         client.call(image);
      }
   }
}


@Component
@RequiredArgsConstructor
class ImageProcessingServiceClient {
   private final OtherClient otherClient;

   @Async
   public CompletableFuture<String>  call(String image) {
      return completedFuture(otherClient.callSync(image));
   }
}

@Component
class OtherClient {

   @SneakyThrows
   public String callSync(String image) {
//      Thread.sleep(100);
      System.out.println("ASTA NU RULEAZA DIN TESTE");
//      return completedFuture(image + " CAT");
      throw new IllegalArgumentException();
   }
}