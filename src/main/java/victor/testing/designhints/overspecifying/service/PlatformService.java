package victor.testing.designhints.overspecifying.service;

import victor.testing.designhints.overspecifying.model.Parcel;

public class PlatformService {
   public void addParcel(Parcel parcel) {
      System.out.println("Platform loaded " + parcel);
   }
}
