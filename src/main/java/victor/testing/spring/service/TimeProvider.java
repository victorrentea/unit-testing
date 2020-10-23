package victor.testing.spring.service;

import java.time.LocalDateTime;

public interface TimeProvider {
	LocalDateTime currentTime();
	
}