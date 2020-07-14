package ro.victor.unittest.teme.Ana;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ NuclearService.class, LocalDateTime.class, LoggerFactory.class })
public class NuclearServiceTest {

    @InjectMocks
    NuclearService nuclearService;

    @Mock
    DistanceService distanceService;

    @Mock
    ProbeService probeService;

    @Mock
    private Logger loggerMock;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private LocalDateTime mockedTime = LocalDateTime.of(2020, Month.JULY, 7, 6, 30, 40, 50000);

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(LocalDateTime.class);

        PowerMockito.mockStatic(LoggerFactory.class);
        PowerMockito.when(LoggerFactory.getLogger(any(Class.class))).thenReturn(loggerMock);
    }

    @Test
    public void interpolateQuantumTooFewAtoms() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Too few atoms");

        int atoms = 9;
        nuclearService.phaserThreshold = atoms - 4;
        nuclearService.interpolateQuantum(atoms, 1);
    }

    @Test
    public void interpolateQuantumTooMuchAtoms() {

        int atoms = 6;
        double result = nuclearService.interpolateQuantum(atoms, 1);
        verify(loggerMock).debug("Processing mass " + atoms);
        assertEquals(6.5, result, 0);
    }

    @Test
    public void interpolateQuantumOk() {

        nuclearService.phaserThreshold = 9;
        int atoms = 15;
        double result = nuclearService.interpolateQuantum(atoms, 1);

        verify(loggerMock).debug("Processing mass " + atoms);
        assertEquals(16.0, result, 0);
    }

    @Test
    public void hasRequiredMassTrue() {
        when(distanceService.distanceTo(2)).thenReturn(2L);
        boolean result = nuclearService.hasRequiredMass(0, 2, 2);

        assertTrue(result);
    }

    @Test
    public void hasRequiredMassFalse() {
        when(distanceService.distanceTo(2)).thenReturn(2L);
        boolean result = nuclearService.hasRequiredMass(2, 2, 2);

        assertFalse(result);
    }

    @Test(expected = ArithmeticException.class)
    public void hasRequiredMassException() {
        when(distanceService.distanceTo(2)).thenReturn(2L);
        boolean result = nuclearService.hasRequiredMass(2, 0, 2);

        assertFalse(result);
    }

    @Test
    public void reportProbeStatsProbeServiceDoesntHaveStatsNear() {

        Coords coords = new Coords();
        int gravity = 0;

        when(probeService.hasStatsNear(coords)).thenReturn(false);
        when(LocalDateTime.now()).thenReturn(mockedTime);

        nuclearService.reportProbeStats(coords, gravity);

        verify(probeService, times(1)).reportStats(coords, gravity, mockedTime);
    }

    @Test
    public void reportProbeStatsProbeServiceHasStatsNear() {

        Coords coords = new Coords();
        int gravity = 0;

        when(probeService.hasStatsNear(coords)).thenReturn(true);
        nuclearService.reportProbeStats(coords, gravity);

        verify(loggerMock).debug("Ignoring duplicate stats at: " + coords);
    }
}