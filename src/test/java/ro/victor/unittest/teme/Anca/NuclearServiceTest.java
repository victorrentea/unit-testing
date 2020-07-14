//package ro.victor.unittest.teme.Anca;
//
//import junit.framework.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//
//
//import java.util.Random;
//
//public class NuclearServiceTest {
//    private NuclearService nuclearService;
//
//    @Before
//    public void setUp() {
//        nuclearService =  new NuclearService();
//    }
//
//    @Test(expected = IllegalStateException.class)
//    public void testinterpolateQuantumThrowsException() {
//        nuclearService.interpolateQuantum(nuclearService.phaserThreshold + 4, new Random().nextDouble());
//    }
//
//    @Test
//    public void testInterpolateQuantumModifiedPhaserThreshold() {
//        nuclearService.phaserThreshold = 7;
//        Assert.assertEquals(14D, nuclearService.interpolateQuantum(13, 1D));
//    }
//
//    @Test
//    public void testinterpolateQuantum() {
//        Assert.assertEquals(6.5D, nuclearService.interpolateQuantum(6, 1D));
//    }
//
//    @Test
//    public void testHasRequiresMass() {
//        DistanceService distanceServiceMock = Mockito.mock(DistanceService.class);
//        nuclearService.setDistanceService(Mockito.mock(DistanceService.class));
//        Mockito.when(distanceServiceMock.distanceTo(Mockito.anyInt())).thenReturn(1L);
//        Assert.assertFalse(nuclearService.hasRequiredMass(1, 5, new Random().nextInt()));
//        Assert.assertTrue(nuclearService.hasRequiredMass(-1, 5, new Random().nextInt()));
//    }
//
//    @Test
//    public void testReportProbeStats() {
//        ProbeService probeServiceMock = Mockito.mock(ProbeService.class);
//        nuclearService.setProbeService(probeServiceMock);
//        Mockito.when(probeServiceMock.hasStatsNear(Mockito.any(Coords.class))).thenReturn(false);
//        Coords coords = new Coords();
//        nuclearService.reportProbeStats(coords, 5D);
//        Mockito.verify(probeServiceMock).reportStats(Mockito.eq(coords), Mockito.eq(5D), Mockito.any());
//
//        probeServiceMock = Mockito.mock(ProbeService.class);
//        nuclearService.setProbeService(probeServiceMock);
//        Mockito.when(probeServiceMock.hasStatsNear(Mockito.any(Coords.class))).thenReturn(true);
//        nuclearService.reportProbeStats(coords, 5D);
//        Mockito.verify(probeServiceMock, Mockito.never()).reportStats(Mockito.eq(coords), Mockito.eq(5D), Mockito.any());
//    }
//
//}
