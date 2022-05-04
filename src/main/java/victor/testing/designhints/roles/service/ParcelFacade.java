package victor.testing.designhints.roles.service;

import lombok.RequiredArgsConstructor;
import victor.testing.designhints.roles.repo.ParcelRepo;
import victor.testing.designhints.roles.repo.TrackingProviderRepo;
import victor.testing.designhints.roles.model.Parcel;
import victor.testing.designhints.roles.model.TrackingProvider;

import java.util.List;

@RequiredArgsConstructor
public class ParcelFacade {
   private final ParcelRepo parcelRepo;
   private final DisplayService displayService;
   private final PlatformService platformService;
   private final TrackingService trackingService;
   private final TrackingProviderRepo trackingProviderRepo;

   public void processBarcode(String barcode, int warehouseId) {
      Parcel parcel = parcelRepo.findByBarcode(barcode);

      displayService.displayAWB(parcel.getAwb());
      if (parcel.isPartOfCompositeShipment()) {
         displayService.displayMultiParcelWarning();
      }
      platformService.addParcel(parcel);
      List<TrackingProvider> trackingProviders = trackingProviderRepo.findByAwb(parcel.getAwb());
      // TODO move findByAwb to trackingService ==> then notice bad encapsulation
      trackingService.markDepartingWarehouse(parcel.getAwb(), warehouseId, trackingProviders);

   }

}
