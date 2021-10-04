package victor.testing.mocks.overspecifying;

import java.util.List;

public interface TrackingService {
   void markDepartingWarehouse(String awb, int warehouseId, List<TrackingProvider> trackingProviders);
}
