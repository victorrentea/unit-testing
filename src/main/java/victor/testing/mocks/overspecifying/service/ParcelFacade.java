package victor.testing.mocks.overspecifying.service;

import lombok.RequiredArgsConstructor;
import victor.testing.mocks.overspecifying.repo.ParcelRepo;
import victor.testing.mocks.overspecifying.repo.TrackingProviderRepo;
import victor.testing.mocks.overspecifying.model.Parcel;

@RequiredArgsConstructor
public class ParcelFacade {
   private final ParcelRepo parcelRepo;
   private final DisplayService displayService;
   private final PlatformService platformService;
   private final TrackingService trackingService;
   private final TrackingProviderRepo trackingProviderRepo;

   public void processBarcode(String barcode, int warehouseId) {
      Parcel parcel = parcelRepo.findByBarcode(barcode);

      displayService.displayAWB(parcel);
      platformService.addParcel(parcel);
      trackingService.markDepartingWarehouse(parcel.getAwb(), warehouseId);
   }
   // option 1: don't test it neah..
   // option 2: delete the function : merge more inside : bring more complexity in
   // option 3: test this not with mock-tests but with integration tests that go e2e through your microservice. (app)
}
