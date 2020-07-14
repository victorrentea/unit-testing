//package ro.victor.unittest.teme.Emilian.Popa;
//
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyDouble;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.time.LocalDateTime;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnit;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.mockito.junit.MockitoRule;
//
//@RunWith(MockitoJUnitRunner.class)
//public class NuclearServiceTest {
//
//	private NuclearService nuclearService;
//	@Mock
//	private DistanceService distanceService;
//	@Mock
//	private ProbeService probeService;
//
//	@Rule
//	public MockitoRule mockitoRule = MockitoJUnit.rule();
//
//
//	@Before
//	public void setup() {
//		nuclearService = new NuclearService();
//		nuclearService.setDistanceService(distanceService);
//		nuclearService.setProbeService(probeService);
//	}
//
//	@Test(expected = IllegalStateException.class)
//	public void testShouldThrowExceptionOnInterpolateQuantum() {
//		nuclearService.interpolateQuantum(2, 2);
//	}
//
//	@Test
//	public void testTooFewAtomsExceptionMessageOnInterpolateQuantum() {
//
//		Exception exception = assertThrows(IllegalStateException.class, () -> nuclearService.interpolateQuantum(2, 2));
//
//		String expectedMessage = "Too few atoms";
//		String actualMessage = exception.getMessage();
//
//		assertTrue(actualMessage.contains(expectedMessage));
//	}
//
//	@Test
//	public void testShouldCalculateInterpolateQuantumWith10AtomsAnd2Mark() {
//		assertEquals(nuclearService.interpolateQuantum(10, 2), 23.00);
//		//can add more tests on different values
//	}
//
//
//	@Test
//	public void hasRequiredMassForTrueResult() {
//		when(distanceService.distanceTo(anyInt())).thenReturn(5L);
//		assertTrue(nuclearService.hasRequiredMass(1, 1, 1));
//	}
//
//	@Test
//	public void hasRequiredMassForNegativResult() {
//		when(distanceService.distanceTo(anyInt())).thenReturn(5L);
//		assertFalse(nuclearService.hasRequiredMass(8, 1, 1));
//	}
//
//	@Test
//	public void testReportProbeStatsTestWhenNoNearStats() {
//		when(probeService.hasStatsNear(any(Coords.class))).thenReturn(false);
//
//		nuclearService.reportProbeStats(new Coords(), 3.00);
//
//		verify(probeService).hasStatsNear(any(Coords.class));
//		verify(probeService).reportStats(any(Coords.class), anyDouble(), any(LocalDateTime.class));
//
//	}
//
//	@Test
//	public void testReportProbeStatsTestWhenNearStats() {
//		when(probeService.hasStatsNear(any(Coords.class))).thenReturn(true);
//
//		nuclearService.reportProbeStats(new Coords(), 3.00);
//
//		verify(probeService).hasStatsNear(any(Coords.class));
//		verify(probeService, never()).reportStats(any(Coords.class), anyDouble(), any(LocalDateTime.class));
//	}
//
//}
