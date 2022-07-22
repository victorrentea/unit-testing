package victor.testing.designhints.roles.repo;

import victor.testing.designhints.roles.model.TrackingProvider;

import java.util.List;

public interface TrackingProviderRepo {
   List<TrackingProvider> findByAwb(String awb);
}
