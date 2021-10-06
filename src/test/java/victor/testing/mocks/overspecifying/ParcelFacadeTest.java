package victor.testing.mocks.overspecifying;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.*;


class ParcelFacadeTest  extends PriceTestBase{
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
//      assertEquals(parcel, event.getParcel());
//      assertEquals(99, event.getWarehouseId());

      //verify(eventPublisher).sendEvent(event...)
   }
   @Captor
   ArgumentCaptor<BarcodeScannedEvent> eventCaptor;
}