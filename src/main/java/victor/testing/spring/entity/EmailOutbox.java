package victor.testing.spring.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class EmailOutbox {
  @Id
  @GeneratedValue
  private Long id;
  private String subject;
  private String body;
  private String recipientEmail;

  public enum Status {
    PENDING,
    SUCCESS,
    ERROR
  }
  @Enumerated(EnumType.STRING)
  private Status status = Status.PENDING;
  @Version
  private Long version;
}
