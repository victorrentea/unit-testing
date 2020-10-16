package victor.testing.fixture;

import org.junit.Before;
import org.junit.Test;

public class Test1 extends SuperTest {
	@Before
	public void initialize2() {
		System.out.println("stuff");
	}
	@Test
	public void test222() {
		System.err.println("2 + " + dummyUserId);
//		ext resouce.clean()
	}
}