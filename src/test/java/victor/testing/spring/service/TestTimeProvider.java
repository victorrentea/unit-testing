package victor.testing.spring.service;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Primary;

@Primary
@org.springframework.stereotype.Component
class TestTimeProvider implements TimeProvider {
	public  LocalDateTime testTime;

	public  LocalDateTime currentTime() {
		if (testTime != null ) {
			return testTime;
		}
		return LocalDateTime.now();
	}
	@Deprecated
	public  void setTestTime(LocalDateTime testTime) {
		this.testTime = testTime;
	}
	public  void clearTestTime() {
		testTime = null;
	}
}