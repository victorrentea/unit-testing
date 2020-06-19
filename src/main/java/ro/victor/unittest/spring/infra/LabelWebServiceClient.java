package ro.victor.unittest.spring.infra;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ro.victor.unittest.spring.domain.Label;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component
public class LabelWebServiceClient {
   @Value("${label.service.base.url}")
   private URI labelServiceUrl;

   public List<Label> retrieveAllLabels() {
      RestTemplate rest = new RestTemplate();
      Labels labels = rest.getForObject(labelServiceUrl + "/other/thing", Labels.class);
      return labels.labels;
   }
}

@Data
class Labels {
   List<Label> labels = new ArrayList<>();
}