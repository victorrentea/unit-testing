package victor.testing.mocks.overspecifying.service;

import lombok.RequiredArgsConstructor;
import victor.testing.mocks.overspecifying.model.Parcel;
import victor.testing.mocks.overspecifying.model.TrackingProvider;
import victor.testing.mocks.overspecifying.repo.TrackingProviderRepo;

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

   public void markDepartingWarehours(int warehouseId, Parcel parcel) {
      List<TrackingProvider> trackingProviders = trackingProviderRepo.findByAwb(parcel.getAwb());
      markDepartingWarehouse(parcel.getAwb(), warehouseId, trackingProviders);
   }
}
