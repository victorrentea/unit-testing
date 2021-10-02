package victor.testing.mocks.overspecifying;

import lombok.Data;

@Data
public class Parcel {
   private String barcode;
   private String awb;
   private boolean partOfCompositeShipment;
}
