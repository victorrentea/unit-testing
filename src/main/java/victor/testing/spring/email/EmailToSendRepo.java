package victor.testing.spring.email;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailToSendRepo extends JpaRepository<EmailToSend, Long> {
  List<EmailToSend> findAllByStatus(EmailToSend.Status status);
}
