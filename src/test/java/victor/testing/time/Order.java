package victor.testing.time;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Order {
   @Id
   @GeneratedValue
   private Long id;
   private Integer customerId;
   private LocalDate createdOn;
   private Double totalAmount;
   private boolean genius;
}
