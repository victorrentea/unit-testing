package victor.testing.mocks.overspecifying.service;

import victor.testing.mocks.overspecifying.model.Parcel;

public class PlatformService {
   public void addParcel(Parcel parcel) {
      System.out.println("Platform loaded " + parcel);
   }
}
