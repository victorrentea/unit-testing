package victor.testing.designhints.overspecifying.repo;

import victor.testing.designhints.overspecifying.model.Parcel;

public interface ParcelRepo {
   Parcel findByBarcode(String barcode);
}
