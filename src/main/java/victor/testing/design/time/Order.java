package victor.testing.design.time;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
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
