package victor.testing.design.roles.service;

import lombok.RequiredArgsConstructor;
import victor.testing.design.roles.model.Parcel;
import victor.testing.design.roles.model.TrackingProvider;
import victor.testing.design.roles.repo.TrackingProviderRepo;

import java.util.List;

@RequiredArgsConstructor
public class TrackingService {
   private final TrackingProviderRepo trackingProviderRepo;

   public void markDepartingWarehouse(Parcel parcel, int warehouseId) {

      List<TrackingProvider> trackingProviders = trackingProviderRepo.findByTrackingNumber(parcel.getTrackingNumber());

      for (TrackingProvider trackingProvider : trackingProviders) {
         System.out.println("Report "+ parcel.getTrackingNumber()+" departing warehouse " + warehouseId
                            + " to " + trackingProvider.getId());
      }
   }
}
