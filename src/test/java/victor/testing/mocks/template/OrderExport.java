package victor.testing.mocks.template;


import victor.testing.time.Order;
import victor.testing.time.OrderRepo;

import java.util.List;

import static java.time.format.DateTimeFormatter.ofPattern;

public class OrderExport extends AbstractFileExport {
   private final OrderRepo orderRepo;

   public OrderExport(EmailSender emailSender, OrderRepo orderRepo) {
      super(emailSender);
      this.orderRepo = orderRepo;
   }

   protected void writeContent() {
      writeLine(List.of("OrderID","Date"));
      for (Order o : orderRepo.findByActiveTrue()) {
         String createdOn = o.getCreatedOn().format(ofPattern("dd MMM yyyy"));
         List<Object> cells = List.of(o.getId(), createdOn);
         writeLine(cells);
      }
   }

}
