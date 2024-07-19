package victor.testing.spring.scheduled;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class EmailToSend {
  @Id
  @GeneratedValue
  private Long id;
  private String subject;
  private String body;
  private String recipientEmail;
//  @NotNull
//  private LocalDateTime sendBy;

  public enum Status {
    TO_SEND,
    SUCCESS,
    ERROR
  }
  @Enumerated(EnumType.STRING)
  private Status status = Status.TO_SEND;
  @Version
  private Long version;
}
