package victor.testing.mocks.overspecifying;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TrackingService {
   private final TrackingProviderRepo trackingProviderRepo;

   public void markDepartingWarehouse(String awb, int warehouseId) {
      List<TrackingProvider> trackingProviders = trackingProviderRepo.findByAwb(awb);

   }
}
