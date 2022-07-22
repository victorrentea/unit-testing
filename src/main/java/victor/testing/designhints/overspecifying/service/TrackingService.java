package victor.testing.designhints.overspecifying.service;

import victor.testing.designhints.overspecifying.model.TrackingProvider;

import java.util.List;

public class TrackingService {
   public void markDepartingWarehouse(String awb, int warehouseId, List<TrackingProvider> trackingProviders) {
      for (TrackingProvider trackingProvider : trackingProviders) {
         System.out.println("Report "+awb+" departing warehouse " + warehouseId
                            + " to " + trackingProvider.getId());
      }
   }
}
