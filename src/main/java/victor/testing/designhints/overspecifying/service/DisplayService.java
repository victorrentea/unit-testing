package victor.testing.designhints.overspecifying.service;

import victor.testing.designhints.overspecifying.model.Parcel;

public class DisplayService {
   public void displayAWB(Parcel parcel){
      System.out.println("Display barcode " + parcel.getBarcode());
   }

   public void displayMultiParcelWarning(){
      System.out.println("Display WARNING: MULTIPARCEL");

   }
}
