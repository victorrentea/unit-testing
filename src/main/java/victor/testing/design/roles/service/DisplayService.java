package victor.testing.design.roles.service;

import victor.testing.design.roles.model.Parcel;

public class DisplayService {
  public void displayParcel(Parcel parcel) {
    if (parcel.isPartOfCompositeShipment()) {
      displayMultiParcelWarning();
    }
    System.out.println("Display barcode " + parcel.getBarcode());
  }

  private void displayMultiParcelWarning() { // encapsulation ++
    System.out.println("Display WARNING: MULTIPARCEL");

  }
}
