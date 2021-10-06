package victor.testing.mocks.overspecifying;

import org.springframework.context.event.EventListener;

public class Display {

   @EventListener
   public void displayAWB(BarcodeScannedEvent e) {
      Parcel parcel = e.getParcel();
      /// my stuff
      if (parcel.isPartOfCompositeShipment()) {
         displayMultiParcelWarning();
      }
   }

   private void displayMultiParcelWarning() {

   }
}