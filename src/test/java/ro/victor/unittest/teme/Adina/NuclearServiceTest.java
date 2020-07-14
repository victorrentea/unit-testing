//package ro.victor.unittest.teme.Adina;
//
//import org.junit.*;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.rules.ExpectedException;
//import org.junit.runners.Parameterized;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoRule;
//
//import java.time.LocalDateTime;
//
//import static org.junit.Assert.assertFalse;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//
//@RunWith(Parameterized.class)
//public class NuclearServiceTest {
//    @Rule
//    private MockitoRule mockitoRule = MockitoJUnit.rule();
//
//    private NuclearService nuclearService = new NuclearService();
//
//    @Parameterized.Parameter(0)
//    public int phaserThreshold;
//
//    @Parameterized.Parameter(1)
//    public int atoms;
//
//    @Parameterized.Parameter(2)
//    public double mark1;
//
//    @Parameterized.Parameters(name = "phaserThreshold is {0} and there are {1} atoms")
//    public static Object[][] params() {
//        return new Object[][] {
//                {5, 9, 10},
//                {5, 10, 7}
//        };
//    }
//
//    @Rule
//    public ExpectedException exceptionRule = ExpectedException.none();
//
//    @Test
//    public void testThrownExceptionAndReturnValue() {
//        nuclearService.phaserThreshold = phaserThreshold;
//
//        try {
//            Assume.assumeTrue(atoms - phaserThreshold < 5);
//            exceptionRule.expect(IllegalStateException.class);
//            nuclearService.interpolateQuantum(atoms, mark1);
//        }
//        catch (AssumptionViolatedException e) {
//            Assertions.assertDoesNotThrow(() -> nuclearService.interpolateQuantum(atoms, mark1), "interpolateQuantum(): Unexpected exception appeared.\n");
//            try {
//                Assume.assumeTrue(atoms <= 2 * phaserThreshold);
//                Assert.assertEquals("interpolateQuantum(): Mark2 should be 0\n", nuclearService.interpolateQuantum(atoms, mark1), atoms * mark1 + mark1 * mark1, 0.0);
//            }
//            catch (AssumptionViolatedException e1) {
//                Assert.assertEquals("interpolateQuantum(): Number of atoms should be bigger than 2 * phaserThreshold\n", nuclearService.interpolateQuantum(atoms, mark1), atoms * mark1 + mark1 * mark1 - mark1 / 2, 0.0);
//            }
//        }
//    }
//
//    @Test
//    public void testRequiredMass() {
//        DistanceService distanceService = mock(DistanceService.class);
//        nuclearService.setDistanceService(distanceService);
//        Mockito.when(distanceService.distanceTo(anyInt())).thenReturn(1L);
//        assertFalse(nuclearService.hasRequiredMass(2, 1, anyInt()));
//        Assert.assertTrue(nuclearService.hasRequiredMass(0, 1, anyInt()));
//    }
//
//    ProbeService probeService = mock(ProbeService.class);
//
//    @Test
//    public void testReportProbeStats() {
//        nuclearService.setProbeService(probeService);
//        Mockito.when(probeService.hasStatsNear(Mockito.any(Coords.class))).thenReturn(true);
//        Coords coords = new Coords();
//        double gravity = Mockito.anyDouble();
//        nuclearService.reportProbeStats(coords, gravity);
//        Mockito.verify(probeService, times(0)).reportStats(Mockito.eq(coords), Mockito.eq(gravity), Mockito.any(LocalDateTime.class));
//
//        Mockito.when(probeService.hasStatsNear(Mockito.any(Coords.class))).thenReturn(false);
//        nuclearService.reportProbeStats(coords, gravity);
//        Mockito.verify(probeService, times(1)).reportStats(Mockito.eq(coords), Mockito.eq(gravity), Mockito.any(LocalDateTime.class));
//    }
//}