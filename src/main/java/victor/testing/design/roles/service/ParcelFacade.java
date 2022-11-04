package victor.testing.design.roles.service;

import lombok.RequiredArgsConstructor;
import victor.testing.design.roles.model.Parcel;
import victor.testing.design.roles.repo.ParcelRepo;
import victor.testing.design.roles.repo.TrackingProviderRepo;

@RequiredArgsConstructor
public class ParcelFacade {
   private final ParcelRepo parcelRepo;
   private final DisplayService displayService;
   private final PlatformService platformService;
   private final TrackingService trackingService;

   public void processBarcode(String barcode, int warehouseId) {
      Parcel parcel = parcelRepo.findByBarcode(barcode);

      displayService.displayAWB(parcel);
      if (parcel.isPartOfCompositeShipment()) {
         displayService.displayMultiParcelWarning();
      }
      platformService.addParcel(parcel);
      // TODO move findByAwb to trackingService ==> then notice bad encapsulation
      trackingService.markDepartingWarehouse(warehouseId, parcel);
   }

}
