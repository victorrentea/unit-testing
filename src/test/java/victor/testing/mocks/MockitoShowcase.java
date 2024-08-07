package victor.testing.mocks;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Dear developer,
 *
 * Please read this file carefully test by test and make sure you understand 100% of it,
 * as it covers the most used mocking framework in the world (as of 2021).
 *
 * Estimated reading time: less than 30 minutes.
 *
 * Please feel free to edit any test to explore what happens.
 * At the beginning of the Unit Testing training you are expected to know everything in this file.
 *
 * Victor
 */

public class MockitoShowcase {
	public interface Dependency { // or concrete class
		String someMethod(String string);
		void sideEffecting(ObjectWithoutEquals object);
	}
	static class ObjectWithoutEquals{
		private final int x;
		ObjectWithoutEquals(int x) {
			this.x = x;
		}
		public int getX() {
			return x;
		}
	}

	private final Dependency mock = mock(Dependency.class);

	private final List<String> mockedList = mock(List.class);

	@Test
	public void verifySomeBehaviour() {

		System.out.println("The mocked class is proxied using CGLIB. Check the class name: " + mockedList.getClass());
		{
			// using mock object
			mockedList.add("one");
			mockedList.clear();
		}

		// verification
		verify(mockedList).add("one");
		verify(mockedList).clear();
	}

	@Test
	public void someStubbing() {
		// You can mock concrete classes, not only interfaces
		LinkedList<String> mock = mock(LinkedList.class);

		// stubbing
		when(mock.get(0)).thenReturn("first");

		{
			// following prints "first"
			System.out.println(mock.get(0));

			// following prints "null" because get(999) was not stubbed
			System.out.println(mock.get(999));
		}
		// - By default, for all methods that return value, mock returns null, an
		// empty collection or appropriate primitive/primitive wrapper value (e.g:
		// 0, false, ... for int/Integer, boolean/Boolean, ...).

		// - Stubbing can be overridden: for example common stubbing can go to
		// @Before setup but the test methods can override it. Please note that
		// overridding stubbing is a potential code smell that points out too much
		// stubbing

		// - Once stubbed, the method will always return stubbed value regardless of
		// how many times it is called.
	}


	// expect an exception to be thrown by the test
	@Test
	public void someStubbingToThrowError() {
		// mock created as a test field by the JUnit Runner

		// stubbing
		when(mockedList.get(anyInt())).thenThrow(new RuntimeException());

		assertThrows(RuntimeException.class,
			() -> System.out.println(mockedList.get(2)));
	}

	@Test
	public void voidMethodToThrowException() {
		doThrow(new RuntimeException()).when(mockedList).clear();

		assertThrows(RuntimeException.class,
			() -> mockedList.clear());
	}

	@Test
	public void argumentMatchers() {
		// stubbing using built-in anyInt() argument matcher
		when(mockedList.get(anyInt())).thenReturn("element");

		System.out.println(mockedList.get(999)); // print "element"
		System.out.println("" + mockedList.contains("111")); // print "true"

		// you can also verify using an argument matcher
		verify(mockedList).get(anyInt());

		// Warning on argument matchers:
		// !!! If you are using argument matchers, all arguments have to be provided by matchers.
		// E.g: (example shows verification but the same applies to stubbing):

		//- verify(mock).someMethod(anyInt(), anyString(), eq("third argument"));
		// above is correct - eq() is also an argument matcher

		//- verify(mock).someMethod(anyInt(), anyString(), "third argument");
		// above is incorrect - exception will be thrown because third argument is given without an argument matcher.
	}

	@Test
	public void verifyNumberOfInvocations_exact() {
		mockedList.add("subscribeOnce");

		//following two verifications work exactly the same - times(1) is used by default
		verify(mockedList).add("subscribeOnce");
		verify(mockedList, times(1)).add("subscribeOnce");

		//verification using never(). never() is an alias to times(0)
		verify(mockedList, never()).add("never happened");
	}

	@Test
	public void verifyNumberOfInvocations_multiple() {
		mockedList.add("three times");
		mockedList.add("three times");
		mockedList.add("three times");

		verify(mockedList, atLeastOnce()).add("three times");
		verify(mockedList, atLeast(2)).add("three times");
		verify(mockedList, atMost(5)).add("three times");
	}


	@Test
	public void stubbingConsecutiveCalls() {
		// Sometimes we need to stub with different return value/exception for
		// the same method call. Typical use case could be mocking iterators.
		when(mock.someMethod("some arg"))
			.thenThrow(new RuntimeException())
			.thenReturn("foo");

		// First call: throws runtime exception:
		assertThrows(RuntimeException.class, () -> mock.someMethod("some arg"));

		// Second call: prints "foo"
		assertEquals("foo", mock.someMethod("some arg"));

		// Any consecutive call: prints "foo" as well (last stubbing prevails).
		assertEquals("foo", mock.someMethod("some arg"));
		System.out.println(mock.someMethod("some arg"));
	}

	@Test
	public void stubbingConsecutiveCalls_shorter() {
		when(mock.someMethod("some arg"))
			.thenReturn("one", "two", "three");

		assertEquals("one", mock.someMethod("some arg")); // first call
		assertEquals("two", mock.someMethod("some arg")); // second call
		assertEquals("three", mock.someMethod("some arg")); // third call

		// Last stubbing prevails
		assertEquals("three", mock.someMethod("some arg"));
	}

	@Test
	public void stubbingWithCallbacks() {
		when(mock.someMethod(anyString())).thenAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			return "called with arguments: " + args[0];// + Math.random();
		});

		String result = mock.someMethod("foo");
		assertEquals("called with arguments: foo", result);
		System.out.println(result);
	}

	@Test
	public void capturingArgumentsForFurtherAssertions() {
		// Mockito verifies argument values in natural java style: by using equals() method.
		// This is the recommended way of matching arguments because it makes tests clean & simple.

		// In some situations though, it is helpful to assert on certain arguments after the actual verification.
		// For example:

		{	// prod code
			ObjectWithoutEquals object = new ObjectWithoutEquals(13);
			mock.sideEffecting(object);
		}

		// or use @Captor annotation on a field
		ArgumentCaptor<ObjectWithoutEquals> argument = ArgumentCaptor.forClass(ObjectWithoutEquals.class);
		verify(mock).sideEffecting(argument.capture());
		// if there is no other way to get the object instance out from tested code
		ObjectWithoutEquals actualArgument = argument.getValue();
		assertEquals(13, actualArgument.getX());

		// Hint: Sometimes the tested code can be refactored to return the object to assert directly

		// Using an ArgumentMatcher can make your tests smaller and more readable
		verify(mock).sideEffecting(argThat(obj -> obj.getX() == 13));
		// + reusable
		verify(mock).sideEffecting(argThat(hasXEqualTo(13)));
	}

	// reusable throughout project
	public static ArgumentMatcher<ObjectWithoutEquals> hasXEqualTo(int x) {
		return obj -> obj.getX() == x;
	}

	@Test
	public void mockLibraryMethod() {
		// prove the original behavior
		assertThrows(RuntimeException.class, () -> SomeLibrary.heavyMethod("x"));

		try (MockedStatic<SomeLibrary> mock = mockStatic(SomeLibrary.class)) {
			// All the calls to static methods of SomeLibrary for this thread are now mocked until the end of try }
			mock.when(() -> SomeLibrary.heavyMethod("x")).thenReturn(2);

			// tested code
			int actual = SomeLibrary.heavyMethod("x") + 5;

			// back in tests
			assertEquals(7, actual);
		}
	}

	public static class SomeLibrary {
		public static int heavyMethod(String x) {
			throw new RuntimeException("Incomplete data, network, files, config, or another weird reason");
		}
	}


	@Test
	public void mockStaticTime() {
		LocalDate fixed = LocalDate.parse("2019-09-29");
		// also see TimeExtensionTest
		try (MockedStatic<LocalDate> mock = mockStatic(LocalDate.class)) {
			// given
			mock.when(LocalDate::now).thenReturn(fixed);

			// when (tested code)
    	String actual = "Current date is " + LocalDate.now();

			// then
			System.out.println(actual);
			assertThat(actual).isEqualTo("Current date is 2019-09-29");
		}
	}

}
