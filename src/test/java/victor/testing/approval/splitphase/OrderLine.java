package victor.testing.approval.splitphase;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderLine {
   private final String productName;
   private final String department;
   private final BigDecimal price;
   private final int quantity;
}
