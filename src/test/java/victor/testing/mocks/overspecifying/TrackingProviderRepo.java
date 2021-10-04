package victor.testing.mocks.overspecifying;

import java.util.List;

public interface TrackingProviderRepo {
   List<TrackingProvider> findByAwb(String awb);
}
