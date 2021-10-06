package victor.testing.mocks.overspecifying;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
public class ParcelFacade {
   private final ParcelRepo parcelRepo;
   private final Display display;
   private final Platform platform;
   private final TrackingService trackingService;

   private final ApplicationEventPublisher eventPublisher;

   public void processBarcode(String barcode, int warehouseId) {
      Parcel parcel = parcelRepo.findByBarcode(barcode);

      eventPublisher.publishEvent(new BarcodeScannedEvent(parcel));

//      display.displayAWB(parcel);
//      platform.addParcel(parcel);
//      trackingService.markDepartingWarehouse(parcel.getAwb(), warehouseId);
   }

}
@Value
class BarcodeScannedEvent {
   Parcel parcel;
}
