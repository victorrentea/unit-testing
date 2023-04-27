package victor.testing.design.roles.service;

import lombok.RequiredArgsConstructor;
import victor.testing.design.roles.model.Parcel;
import victor.testing.design.roles.repo.ParcelRepo;

@RequiredArgsConstructor
public class ParcelFacade {
   private final ParcelRepo parcelRepo;
   private final DisplayService displayService;
   private final PlatformService platformService;
   private final TrackingService trackingService;

   public void processBarcode(String barcode, int warehouseId) {
      Parcel parcel = parcelRepo.findByBarcode(barcode);

      displayService.displayParcel(parcel);

      platformService.addParcel(parcel);

      trackingService.markDepartingWarehouse(parcel, warehouseId);
   }

   // Middle Man code smell: metoda inutila; ditstruge-o ?
//   public String getStatus(String barcode) {
//      return parcelRepo.findByBarcode(barcode).getStatus();
//   }
//   public String getStatus(String barcode) {
//      return blueRepo.findByBarcode(barcode).getStatus();
//   }
//   public String getStatus(String barcode) {
//      return greenRepo.findByBarcode(barcode).getStatus();
//   }

}
