package victor.testing.mocks.overspecifying;

public class Display {
   public void displayAWB(Parcel parcel) {
      /// my stuff
      if (parcel.isPartOfCompositeShipment()) {
         displayMultiParcelWarning();
      }
   }

   private void displayMultiParcelWarning() {

   }
}
