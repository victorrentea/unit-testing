package victor.testing.designhints.roles.service;

import lombok.RequiredArgsConstructor;
import victor.testing.designhints.roles.model.Parcel;
import victor.testing.designhints.roles.model.TrackingProvider;
import victor.testing.designhints.roles.repo.TrackingProviderRepo;

import java.util.List;

@RequiredArgsConstructor
public class TrackingService {
   private final TrackingProviderRepo trackingProviderRepo;

   public void markDepartingWarehouse(Parcel parcel, int warehouseId) {
      List<TrackingProvider> trackingProviders = trackingProviderRepo.findByAwb(parcel.getAwb());
      for (TrackingProvider trackingProvider : trackingProviders) {
         System.out.println("Report "+ parcel.getAwb()+" departing warehouse " + warehouseId
                            + " to " + trackingProvider.getId());
      }
   }
}
