package victor.testing.design.roles.service;

import victor.testing.design.roles.model.Parcel;

public class DisplayService {
   public void displayParcel(Parcel parcel){
      System.out.println("Display barcode " + parcel.getBarcode());
      if (parcel.isPartOfCompositeShipment()) {
         displayMultiParcelWarning();
      }
   }

   public void displayMultiParcelWarning(){
      System.out.println("Display WARNING: MULTIPARCEL");

   }
}
