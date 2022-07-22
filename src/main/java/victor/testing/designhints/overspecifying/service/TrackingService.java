package victor.testing.designhints.overspecifying.service;

import lombok.RequiredArgsConstructor;
import victor.testing.designhints.overspecifying.model.TrackingProvider;
import victor.testing.designhints.overspecifying.repo.TrackingProviderRepo;

import java.util.List;

@RequiredArgsConstructor
public class TrackingService {
   private final TrackingProviderRepo trackingProviderRepo;

   public void markDepartingWarehouse(String awb, int warehouseId, List<TrackingProvider> trackingProviders) {
      for (TrackingProvider trackingProvider : trackingProviders) {
         System.out.println("Report "+awb+" departing warehouse " + warehouseId
                            + " to " + trackingProvider.getId());
      }
   }
}
