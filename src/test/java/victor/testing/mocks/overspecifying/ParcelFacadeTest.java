package victor.testing.mocks.overspecifying;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.overspecifying.repo.ParcelRepo;
import victor.testing.mocks.overspecifying.repo.TrackingProviderRepo;
import victor.testing.mocks.overspecifying.service.DisplayService;
import victor.testing.mocks.overspecifying.model.Parcel;
import victor.testing.mocks.overspecifying.service.ParcelFacade;
import victor.testing.mocks.overspecifying.service.PlatformService;
import victor.testing.mocks.overspecifying.model.TrackingProvider;
import victor.testing.mocks.overspecifying.service.TrackingService;

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

//      verify(displayService).displayAWB("AWB");
//      verify(displayService).displayMultiParcelWarning();
      verify(displayService).displayParcel(parcel);
      verify(platformService).addParcel(parcel);
      verify(trackingService).markDepartingWarehours(99, parcel);
   }
}