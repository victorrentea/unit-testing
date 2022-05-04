package victor.testing.designhints.roles.service;

import victor.testing.designhints.roles.model.Parcel;

public class PlatformService {
   public void addParcel(Parcel parcel) {
      System.out.println("Platform loaded " + parcel);
   }
}
