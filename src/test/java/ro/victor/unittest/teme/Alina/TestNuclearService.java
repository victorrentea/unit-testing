//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnit;
//import org.mockito.junit.MockitoRule;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@RunWith(JUnit4.class)
//public final class TestNuclearService {
//    @Mock
//    private DistanceService mockDistanceService;
//    @Mock
//    private ProbeService mockProbeService;
//    @Mock
//    private Logger mockLogger;
//
//    @Rule
//    public MockitoRule mockitoRule = MockitoJUnit.rule();
//
//    NuclearService nuclearService;
//
//
//    @Before
//    public final void init() {
//        MockitoAnnotations.initMocks(this);
//        nuclearService = new NuclearService(2, mockLogger, mockDistanceService, mockProbeService);
//
//    }
//
//    @Test(expected = IllegalStateException.class)
//    public void testIllegalStateException_at_interpolateQuantum() {
//        nuclearService.interpolateQuantum(2, 1.0);
//    }
//
//    @Test
//    public void testLogger_at_interpolateQuantum() {
//        // no idea who is org.slf4j.Loggeri but let's test it
//        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
//
//        nuclearService.interpolateQuantum(15, 1.0);
//
//        verify(mockLogger).debug(captor.capture());
//        assertEquals("Processing mass 15", captor.getValue());
//    }
//
//    @Test
//    public void testResult_at_interpolateQuantum() {
//        final double value = nuclearService.interpolateQuantum(15, 1.0);
//        assertEquals(15.5, value, 0.0);
//    }
//
//    @Test
//    public void test_massTooBig() {
//        final int destinationSolarSystemId = 100;
//        final long distanceTo = 5000L;
//        final int quanta = 100000;
//
//        when(mockDistanceService.distanceTo(destinationSolarSystemId)).thenReturn(distanceTo);
//
//        boolean value = nuclearService.hasRequiredMass(quanta, 2, destinationSolarSystemId);
//        assertFalse(value);
//    }
//
//    @Test
//    public void test_massOK() {
//        final int destinationSolarSystemId = 100;
//        final long distanceTo = 5000L;
//        final int quanta = 2499;
//
//        when(mockDistanceService.distanceTo(destinationSolarSystemId)).thenReturn(distanceTo);
//
//        boolean value = nuclearService.hasRequiredMass(quanta, 2, destinationSolarSystemId);
//        assertTrue(value);
//    }
//
//    @Test
//    public void testLogger_at_reportProbeStats() {
//        // no idea who is org.slf4j.Loggeri but let's test it
//        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
//        final Coords coords = new Coords();
//
//        when(mockProbeService.hasStatsNear(coords)).thenReturn(true);
//
//        nuclearService.reportProbeStats(coords, 9.8);
//
//        verify(mockLogger).debug(captor.capture());
//        assertEquals("Ignoring duplicate stats at: " + coords, captor.getValue());
//    }
//
//    @Test
//    public void testReportStats() {
//        final Coords coords = new Coords();
//        final double gravity = 9.8;
//        final ArgumentCaptor<Coords> captor = ArgumentCaptor.forClass(Coords.class);
//        final ArgumentCaptor<Double> captorGravity = ArgumentCaptor.forClass(Double.class);
//
////        coords = null;
//        when(mockProbeService.hasStatsNear(coords)).thenReturn(false);
//
//        nuclearService.reportProbeStats(coords, gravity);
//
//        verify(mockProbeService).reportStats(captor.capture(), captorGravity.capture(), any());
//        assertEquals(coords, captor.getValue());
//        assertEquals(gravity, captorGravity.getValue(), 0.0);
//    }
//}
