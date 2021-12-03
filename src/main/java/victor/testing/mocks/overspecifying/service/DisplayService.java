package victor.testing.mocks.overspecifying.service;

import victor.testing.mocks.overspecifying.model.Parcel;

public class DisplayService {
   private void displayAWB(String barcode){
      System.out.println("Display barcode " + barcode);
   }

   private void displayMultiParcelWarning(){
      System.out.println("Display WARNING: MULTIPARCEL");

   }

   public void displayParcel(Parcel parcel) {
      displayAWB(parcel.getAwb());
      if (parcel.isPartOfCompositeShipment()) {
         displayMultiParcelWarning();
      }
   }
}
