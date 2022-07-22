package victor.testing.designhints.overspecifying.repo;

import victor.testing.designhints.overspecifying.model.TrackingProvider;

import java.util.List;

public interface TrackingProviderRepo {
   List<TrackingProvider> findByAwb(String awb);
}
