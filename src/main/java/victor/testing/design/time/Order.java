package victor.testing.design.time;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Table(name="ORDERS")
public class Order {
   @Id
   @GeneratedValue
   private Long id;
   private Integer customerId;
   private LocalDate createdOn;
   private Double totalAmount;
   private boolean genius;
   private boolean active;
}
