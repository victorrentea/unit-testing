package victor.testing.designhints.roles.repo;

import victor.testing.designhints.roles.model.Parcel;

public interface ParcelRepo {
   Parcel findByBarcode(String barcode);
}
