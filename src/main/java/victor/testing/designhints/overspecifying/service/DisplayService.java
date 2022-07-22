package victor.testing.designhints.overspecifying.service;

public class DisplayService {
   public void displayAWB(String barcode){
      System.out.println("Display barcode " + barcode);
   }

   public void displayMultiParcelWarning(){
      System.out.println("Display WARNING: MULTIPARCEL");

   }
}
