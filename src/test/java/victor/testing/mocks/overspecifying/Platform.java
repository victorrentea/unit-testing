package victor.testing.mocks.overspecifying;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Platform {
   @EventListener
   public void addParcel(BarcodeScannedEvent event) {
      Parcel parcel = event.getParcel();
      System.out.println("Logic ...  " + parcel);
   }
}
