package ro.victor.unittest.spring.facade;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Service
@RequiredArgsConstructor
public class ParallelFlow {
   private final ImageProcessingServiceClient client;


   @SneakyThrows
   public void doIt(String image) {
      List<Future<String>> all = new ArrayList<>();
      for (int i = 0; i < 5; i++) {
         all.add(client.call(image));
      }
      for (Future<String> stringFuture : all) {
         System.out.println(stringFuture.get());
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
   @Value("${image.service.url}")
   private URL url;

   @SneakyThrows
   public String callSync(String image) {
//      Thread.sleep(100);
      System.out.println("ASTA NU RULEAZA DIN TESTE");
//      return completedFuture(image + " CAT");

      RestTemplate rest = new RestTemplate();
      return rest.getForObject(url.toURI().toString() + "/stuff", String.class);

   }
}