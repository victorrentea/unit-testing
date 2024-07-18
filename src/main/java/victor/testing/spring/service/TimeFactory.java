package victor.testing.spring.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TimeFactory {
  public LocalDateTime now() {
    return LocalDateTime.now();
  }
}
