package victor.testing.mocks.overspecifying.service;

import victor.testing.mocks.overspecifying.model.Parcel;

public class DisplayService {
   public void displayAWB(Parcel parcel){
      System.out.println("Display barcode " + parcel.getAwb());
      if (parcel.isPartOfCompositeShipment()) {
         displayMultiParcelWarning();
      }
   }

   private void displayMultiParcelWarning(){
      System.out.println("Display WARNING: MULTIPARCEL");

   }
}
