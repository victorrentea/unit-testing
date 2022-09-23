package victor.testing.design.roles.repo;

import victor.testing.design.roles.model.TrackingProvider;

import java.util.List;

public interface TrackingProviderRepo {
   List<TrackingProvider> findByAwb(String awb);
}
