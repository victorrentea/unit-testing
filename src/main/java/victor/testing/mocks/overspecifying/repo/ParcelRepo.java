package victor.testing.mocks.overspecifying.repo;

import victor.testing.mocks.overspecifying.model.Parcel;

public interface ParcelRepo {
   Parcel findByBarcode(String barcode);
}
