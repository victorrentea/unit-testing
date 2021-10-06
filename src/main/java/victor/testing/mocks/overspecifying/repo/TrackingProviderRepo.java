package victor.testing.mocks.overspecifying.repo;

import victor.testing.mocks.overspecifying.model.TrackingProvider;

import java.util.List;

public interface TrackingProviderRepo {
   List<TrackingProvider> findByAwb(String awb);
}
