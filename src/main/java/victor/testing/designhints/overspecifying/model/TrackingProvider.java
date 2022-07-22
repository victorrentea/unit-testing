package victor.testing.designhints.overspecifying.model;

import lombok.Data;

import java.util.UUID;

@Data
public class TrackingProvider {
   private String id = UUID.randomUUID().toString();
}
