package victor.testing.designhints.roles.service;

import lombok.RequiredArgsConstructor;
import victor.testing.designhints.roles.model.Parcel;
import victor.testing.designhints.roles.repo.ParcelRepo;
import victor.testing.designhints.roles.repo.TrackingProviderRepo;

@RequiredArgsConstructor
public class ParcelOrchestrator {
   private final ParcelRepo parcelRepo;
   private final DisplayService displayService;
   private final PlatformService platformService;
   private final TrackingService trackingService;
   private final TrackingProviderRepo trackingProviderRepo;

   public void processBarcode(String barcode, int warehouseId) {
      Parcel parcel = parcelRepo.findByBarcode(barcode);

      displayService.displayAWB(parcel);
      if (parcel.isPartOfCompositeShipment()) {
         displayService.displayMultiParcelWarning();
      }
      platformService.addParcel(parcel);

      // TODO move findByAwb to trackingService ==> then notice bad encapsulation
      trackingService.markDepartingWarehouse(parcel, warehouseId);
   }

}
