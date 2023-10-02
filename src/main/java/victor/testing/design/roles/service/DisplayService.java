package victor.testing.design.roles.service;

import victor.testing.design.roles.model.Parcel;

public class DisplayService {
   public void displayParcel(Parcel parcel){
      System.out.println("Display info on scanner " + parcel.getBarcode());
   }

   public void displayMultiParcelWarning(){
      System.out.println("Display ⚠️MULTIPARCEL");
   }
}
