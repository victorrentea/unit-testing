package victor.testing.mocks.overspecifying;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ParcelFacade {
   private final ParcelRepo parcelRepo;
   private final Display display;
   private final Platform platform;
   private final TrackingService trackingService;
   private final TrackingProviderRepo trackingProviderRepo;

   public void processBarcode(String barcode, int warehouseId) {
      Parcel parcel = parcelRepo.findByBarcode(barcode);

      display.displayAWB(parcel.getAwb());
      if (parcel.isPartOfCompositeShipment()) {
         display.displayMultiParcelWarning();
      }
      platform.addParcel(parcel);
      List<TrackingProvider> trackingProviders = trackingProviderRepo.findByAwb(parcel.getAwb());
      // TODO move findByAwb to trackingService ==> then notice bad encapsulation
      trackingService.markDepartingWarehouse(parcel.getAwb(), warehouseId, trackingProviders);

   }

}
