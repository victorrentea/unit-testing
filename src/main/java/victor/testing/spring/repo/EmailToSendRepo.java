package victor.testing.spring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.testing.spring.entity.EmailToSend;

import java.util.List;

public interface EmailToSendRepo extends JpaRepository<EmailToSend, Long> {
  List<EmailToSend> findAllByStatus(EmailToSend.Status status);
}
