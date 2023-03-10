package victor.testing.design.roles.service;

import victor.testing.design.roles.model.Parcel;

public class DisplayService {

   public void displayAWBWithWarning(Parcel parcel) { // used by others
      displayAWB(parcel);
      if (parcel.isPartOfCompositeShipment()) {
         displayMultiParcelWarning();
      }
   }
   public void displayAWB(Parcel parcel){ // used by others
      System.out.println("Display barcode " + parcel.getBarcode());
   }

   public void displayMultiParcelWarning(){
      System.out.println("Display WARNING: MULTIPARCEL");

   }
}
