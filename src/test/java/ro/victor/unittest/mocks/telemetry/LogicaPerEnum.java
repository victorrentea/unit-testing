package ro.victor.unittest.mocks.telemetry;

import java.util.function.Consumer;

public class LogicaPerEnum {

   public static void handleMessage(MessageCode code, String param) {
//      switch (code) {
//         case A: handleA(param);break;
//         case B: handleB(param);break;
//         default:
//            throw new IllegalStateException("Unexpected value: " + code);
//      }
      code.handler.accept(param);
   }

   public static void handleA(String s) {
   }

   public static void handleB(String s) {
   }
//   @Test
//   public void test() {
//      for (MessageCode code : MessageCode.values()) {
//         handleA(code, "");
//      }
//   }

}

enum MessageCode {
   A(LogicaPerEnum::handleA),
   B(LogicaPerEnum::handleB),
//      F(???)
   ;
   public final Consumer<String> handler;

   MessageCode(Consumer<String> handler) {
      this.handler = handler;
   }
}

class AltaClasa {
   {
//      MessageCode
   }
}
