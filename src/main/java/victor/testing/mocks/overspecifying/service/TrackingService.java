package victor.testing.mocks.overspecifying.service;

import victor.testing.mocks.overspecifying.model.Parcel;
import victor.testing.mocks.overspecifying.model.TrackingProvider;
import victor.testing.mocks.overspecifying.repo.TrackingProviderRepo;

import java.util.List;

public class TrackingService {
   private final TrackingProviderRepo trackingProviderRepo;

   public TrackingService(TrackingProviderRepo trackingProviderRepo) {
      this.trackingProviderRepo = trackingProviderRepo;
   }

   public void markDepartingWarehouse(int warehouseId, Parcel parcel) {
      List<TrackingProvider> trackingProviders = trackingProviderRepo.findByAwb(parcel.getAwb());

      for (TrackingProvider trackingProvider : trackingProviders) {
         System.out.println("Report "+parcel.getAwb()+" departing warehouse " + warehouseId
                            + " to " + trackingProvider.getId());
      }
   }
}
