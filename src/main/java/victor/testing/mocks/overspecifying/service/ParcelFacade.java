package victor.testing.mocks.overspecifying.service;

import lombok.RequiredArgsConstructor;
import victor.testing.mocks.overspecifying.model.Parcel;
import victor.testing.mocks.overspecifying.repo.ParcelRepo;

@RequiredArgsConstructor
public class ParcelFacade {
   private final ParcelRepo parcelRepo;
   private final DisplayService displayService;
   private final PlatformService platformService;
   private final TrackingService trackingService;

   public void processBarcode(String barcode, int warehouseId) {
      Parcel parcel = parcelRepo.findByBarcode(barcode);

      displayService.displayAWB(parcel);
      platformService.addParcel(parcel);
      trackingService.markDepartingWarehouse(parcel.getAwb(), warehouseId);
   }

}
