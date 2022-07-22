package victor.testing.designhints.overspecifying;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.designhints.overspecifying.repo.ParcelRepo;
import victor.testing.designhints.overspecifying.repo.TrackingProviderRepo;
import victor.testing.designhints.overspecifying.service.DisplayService;
import victor.testing.designhints.overspecifying.model.Parcel;
import victor.testing.designhints.overspecifying.service.ParcelFacade;
import victor.testing.designhints.overspecifying.service.PlatformService;
import victor.testing.designhints.overspecifying.model.TrackingProvider;
import victor.testing.designhints.overspecifying.service.TrackingService;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParcelFacadeTest {
   @Mock
   ParcelRepo parcelRepo;
   @Mock
   DisplayService displayService;
   @Mock
   PlatformService platformService;
   @Mock
   TrackingService trackingService;
   @Mock
   TrackingProviderRepo trackingProviderRepo;
   @InjectMocks
   ParcelFacade target;

   @Test
   void processBarcode() {
      Parcel parcel = new Parcel()
          .setBarcode("BAR")
          .setAwb("AWB")
          .setPartOfCompositeShipment(true);
      when(parcelRepo.findByBarcode("BAR")).thenReturn(parcel);
      List<TrackingProvider> trackingProviders = List.of(new TrackingProvider());
      when(trackingProviderRepo.findByAwb("AWB")).thenReturn(trackingProviders);

      target.processBarcode("BAR", 99);

      verify(displayService).displayAWB(parcel);
      verify(displayService).displayMultiParcelWarning();
      verify(platformService).addParcel(parcel);
      verify(trackingService).markDepartingWarehouse("AWB", 99, trackingProviders);
   }
}