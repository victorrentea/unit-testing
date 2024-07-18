package victor.testing.spring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.testing.spring.entity.EmailOutbox;

import java.util.List;

public interface EmailToSendRepo extends JpaRepository<EmailOutbox, Long> {
  List<EmailOutbox> findAllByStatus(EmailOutbox.Status status);
}
