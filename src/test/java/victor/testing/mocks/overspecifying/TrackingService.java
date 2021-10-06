package victor.testing.mocks.overspecifying;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackingService {
   private final TrackingProviderRepo trackingProviderRepo;

   @EventListener
   public void markDepartingWarehouse(BarcodeScannedEvent event) {
      int warehouseId = event.getWarehouseId();
      Parcel parcel = event.getParcel();
      List<TrackingProvider> trackingProviders = trackingProviderRepo.findByAwb(parcel.getAwb());

   }
}
