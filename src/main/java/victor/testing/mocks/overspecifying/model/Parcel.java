package victor.testing.mocks.overspecifying.model;

import lombok.Data;

@Data
public class Parcel {
   private String barcode;
   private String awb;
   private boolean partOfCompositeShipment;
}
