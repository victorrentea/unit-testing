package victor.testing.design.roles.service;

import lombok.RequiredArgsConstructor;
import victor.testing.design.roles.model.TrackingProvider;
import victor.testing.design.roles.repo.TrackingProviderRepo;

import java.util.List;

@RequiredArgsConstructor
public class TrackingService {
   private final TrackingProviderRepo trackingProviderRepo;

   public void markDepartingWarehouse(String awb, int warehouseId) {
      List<TrackingProvider> trackingProviders = trackingProviderRepo.findByAwb(awb);

      for (TrackingProvider trackingProvider : trackingProviders) {
         System.out.println("Report "+awb+" departing warehouse " + warehouseId
                            + " to " + trackingProvider.getId());
      }
   }
}
