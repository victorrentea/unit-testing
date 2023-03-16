package victor.testing.design.onion.lib;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Contract {
  public enum Status {
    DRAFT, ACTIVE,CLOSED
  }
  private String number;
  private String name;
  private LocalDateTime lastPayment;
  private Status status;
  private Double remainingValue;
}
