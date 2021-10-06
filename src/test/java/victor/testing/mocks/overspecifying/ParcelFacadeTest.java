package victor.testing.mocks.overspecifying;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParcelFacadeTest {
   @Mock
   ParcelRepo parcelRepo;
   @Mock
   ApplicationEventPublisher eventPublisher;
   @InjectMocks
   ParcelFacade target;

   @Test
   void processBarcode() {
      Parcel parcel = new Parcel()
          .setBarcode("BAR")
          .setAwb("AWB")
          .setPartOfCompositeShipment(true);
      when(parcelRepo.findByBarcode("BAR")).thenReturn(parcel);

      target.processBarcode("BAR", 99);

      verify(eventPublisher).publishEvent(eventCaptor.capture());
      BarcodeScannedEvent event = eventCaptor.capture();
      assertEquals(parcel, event.getParcel());
      assertEquals(99, event.getWarehouseId());

      //verify(eventPublisher).sendEvent(event...)
   }
   @Captor
   ArgumentCaptor<BarcodeScannedEvent> eventCaptor;
}