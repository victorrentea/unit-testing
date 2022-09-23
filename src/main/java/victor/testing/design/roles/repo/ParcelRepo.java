package victor.testing.design.roles.repo;

import victor.testing.design.roles.model.Parcel;

public interface ParcelRepo {
   Parcel findByBarcode(String barcode);
}
