package victor.testing.mocks.overspecifying;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParcelFacadeTest {
   @Mock
   ParcelRepo parcelRepo;
   @Mock
   Display display;
   @Mock
   Platform platform;
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

      verify(display).displayAWB("AWB");
      verify(display).displayMultiParcelWarning();
      verify(platform).addParcel(parcel);
      verify(trackingService).markDepartingWarehouse("AWB", 99, trackingProviders);
   }
}