package ro.victor.unittest.mocks;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({OldSingleton.class, LocalDateTime.class})
public class PowerToTheMasses {
	public static final String CONST = "a";

	private String productionCode() {
		OldSingleton mock = OldSingleton.getInstance();
		return mock.getState().toUpperCase();
	}

	@Test
	public void productionCodeReturnsSingletonStateToUpper() {
		PowerMockito.mockStatic(OldSingleton.class);
		OldSingleton tzeapa = mock(OldSingleton.class);
		when(OldSingleton.getInstance()).thenReturn(tzeapa);
		when(tzeapa.getState()).thenReturn("a");

		String rez = productionCode();

		assertEquals("A", rez);


		PowerMockito.mockStatic(System.class);
		PowerMockito.mockStatic(LocalDateTime.class);
		when(System.currentTimeMillis()).thenReturn(0l);
		LocalDateTime fixed = LocalDateTime.of(2019, 01, 01, 0, 0);
		when(LocalDateTime.now()).thenReturn(fixed);

		System.out.println(System.currentTimeMillis());
		System.out.println(System.currentTimeMillis());
		System.out.println(LocalDateTime.now());
		System.out.println(new Date());
	}

}

class OldSingleton{
	private static OldSingleton INSTANCE;
	private OldSingleton() {
	}
	public static OldSingleton getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new OldSingleton();
		}
//			return INSTANCE;
		throw new IllegalArgumentException();
	}
	private String state;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}