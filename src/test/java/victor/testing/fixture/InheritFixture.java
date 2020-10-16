package victor.testing.fixture;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InheritFixture {

}

abstract class Again {
	
}
abstract class SuperTest extends Again{ 
	
	protected Long dummyUserId;
	// prepare data . mocks. db. ext resouce
	// insert dummy user in an in-mem db
	// insert reference data 
	// configure current fake request data.
	@Before
	public final void initialize() {
		System.out.println("INIT");
	}

	//cleanup 
	@After
	public void test() {
		System.err.println("1 - check resources are clear");
//		assertEquals(); // ok 
	}
}
