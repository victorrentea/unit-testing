package victor.testing.mocks.overspecifying;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParcelFacade {
   private final ParcelRepo parcelRepo;
   private final Display display;
   private final Platform platform;
   private final TrackingService trackingService;

   public void processBarcode(String barcode, int warehouseId) {
      Parcel parcel = parcelRepo.findByBarcode(barcode);

      display.displayAWB(parcel.getAwb());
      if (parcel.isPartOfCompositeShipment()) {
         display.displayMultiParcelWarning();
      }
      platform.addParcel(parcel);
      trackingService.markDepartingWarehouse(parcel.getAwb(), warehouseId);
   }

}
