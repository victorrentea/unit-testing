//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnit;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.mockito.junit.MockitoRule;
//
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class NuclearServiceTest {
////    @Rule
////    public MockitoRule rule = MockitoJUnit.rule();
//
//    private NuclearService nuclearService;
//
//    @Mock
//    private DistanceService distanceService;
//
//    @Mock
//    private ProbeService probeService;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        nuclearService = new NuclearService(2, distanceService, probeService);
//    }
//
//    @Test
//    public void interpolateQuantumShould() {
//        nuclearService = new NuclearService(8, null, null);
//        assertEquals(24.75, nuclearService.interpolateQuantum(15, 1.5), 0.0);
//    }
//
//    @Test(expected = IllegalStateException.class)
//    public void interpolateQuantumShouldthrowException() {
//        nuclearService = new NuclearService(2, null, null);
//        nuclearService.interpolateQuantum(3, 1.5);
//    }
//
//    @Test
//    public void hasRequiredMass_massOK() {
//        when(distanceService.distanceTo(anyInt())).thenReturn(1000L);
//        boolean result = nuclearService.hasRequiredMass(4, 20, 2);
//        assertTrue(result);
//    }
//
//    @Test
//    public void hasRequiredMass_massInvalid() {
//        when(distanceService.distanceTo(anyInt())).thenReturn(10L);
//        boolean result = nuclearService.hasRequiredMass(4, 20, 2);
//        assertFalse(result);
//    }
//
//    @Test
//    public void reportProbeStatsShould() {
//        Coords coords = new Coords();
//        when(probeService.hasStatsNear(coords)).thenReturn(false);
//        nuclearService.reportProbeStats(coords, 9.8);
//        verify(probeService).reportStats(eq(coords), eq(9.8), any());
//    }
//
//    @Test
//    public void reportProbeStats_testLogger() {
//        Coords coords = new Coords();
//        when(probeService.hasStatsNear(coords)).thenReturn(true);
//        nuclearService.reportProbeStats(coords, 9.8);
//        verify(probeService, times(0)).reportStats(eq(coords), eq(9.8), any());
//    }
//
//}
