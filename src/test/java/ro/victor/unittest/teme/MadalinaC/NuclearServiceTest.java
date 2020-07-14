//package ro.victor.unittest.teme.MadalinaC;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//
//import java.time.LocalDateTime;
//import org.mockito.junit.MockitoJUnitRunner;
//import ro.victor.unittest.prework.Coords;
//import ro.victor.unittest.prework.DistanceService;
//import ro.victor.unittest.prework.NuclearService;
//import ro.victor.unittest.prework.ProbeService;
//
//import static org.mockito.Mockito.*;
//
//
//@RunWith(MockitoJUnitRunner.class)
//public class NuclearServiceTest {
//    @InjectMocks
//    NuclearService nuclearService;
//
//    @Mock
//    DistanceService distanceServiceMock;
//
//    @Mock
//    ProbeService probeServiceMock;
//
//    @Test(expected = IllegalStateException.class)
//    public void interpolateQuantumTooFewAtoms(){
//        int atoms = 4;
//        double mark1 =2;
//        nuclearService.interpolateQuantum(atoms, mark1);
//    }
//
//    @Test
//    public void interpolateQuantumNoAtoms(){
//        int atoms = 5;
//        double mark1 = 2;
//        double result = nuclearService.interpolateQuantum(atoms, mark1);
//        Assert.assertEquals(atoms * mark1 + mark1 * mark1-mark1 / 2, result,0);
//    }
//
//    @Test
//    public void testHasRequiredMass(){
//        int quanta=3;
//        int speed=5;
//
//        when(distanceServiceMock.distanceTo(anyInt())).thenReturn(speed*(quanta+1L));
//
//        boolean result = nuclearService.hasRequiredMass(quanta, speed, anyInt());
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void testReportProbeStats(){
//
//        ArgumentCaptor<LocalDateTime> argument = ArgumentCaptor.forClass(LocalDateTime.class);
//
//        Coords coord = mock(Coords.class);
//        double gravity = 5;
//        when(probeServiceMock.hasStatsNear(eq(coord))).thenReturn(false);
//        nuclearService.reportProbeStats(coord, gravity);
//
//        verify(probeServiceMock, atLeast(1)).reportStats(eq(coord), eq(gravity), argument.capture());
//        LocalDateTime time = argument.getValue();
//        Assert.assertTrue(time.isBefore(LocalDateTime.now()));
//    }
//
//    @Test
//    public void testNoReportProbeStats(){
//
//        Coords coord = mock(Coords.class);
//        double gravity = 5;
//        when(probeServiceMock.hasStatsNear(coord)).thenReturn(true);
//        nuclearService.reportProbeStats(coord, gravity);
//
//        verify(probeServiceMock, times(0)).reportStats(any(Coords.class), anyDouble(), any(LocalDateTime.class));
//
//    }
//}
