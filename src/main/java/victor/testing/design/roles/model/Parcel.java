package victor.testing.design.roles.model;

import lombok.Data;

@Data
public class Parcel {
   private String barcode;
   private String trackingNumber;
   private boolean partOfCompositeShipment;
}
