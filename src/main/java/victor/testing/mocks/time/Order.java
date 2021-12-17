package victor.testing.mocks.time;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Data
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
