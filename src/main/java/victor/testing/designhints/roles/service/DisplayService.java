package victor.testing.designhints.roles.service;

import victor.testing.designhints.roles.model.Parcel;

public class DisplayService {
   public void displayAWB(Parcel parcel){
      System.out.println("Display barcode " + parcel.getBarcode());
   }

   public void displayMultiParcelWarning(){
      System.out.println("Display WARNING: MULTIPARCEL");

   }
}
