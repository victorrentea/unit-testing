package victor.testing.designhints.roles.model;

import lombok.Data;

import java.util.UUID;

@Data
public class TrackingProvider {
   private String id = UUID.randomUUID().toString();
}
