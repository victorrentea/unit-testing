package victor.testing.spring.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

//public interface TimeProvider {
//	LocalDateTime currentTime();
//	
//}

@Component
public class TimeProvider {

	public LocalDateTime currentTime() {
		return LocalDateTime.now();
	}
}