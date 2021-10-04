package victor.testing.mocks.template;

public interface EmailSender {
   void sendErrorEmail(String export_orders, Exception e);
}
