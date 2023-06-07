package victor.testing.spring.email;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.*;

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
