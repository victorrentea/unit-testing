//package ro.victor.unittest.teme.Alexandra;
//
//
//
///**
//import org.junit.runner.RunWith;
//
//import org.mockito.junit.MockitoJUnitRunner;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.when;
//**/
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.mockito.Mockito;
//import ro.victor.unittest.prework.NuclearService;
//
//import java.util.Random;
//
//import static org.junit.Assert.*;
//
//public class NuclearServiceTest {
//
//    @Test
//    public void interpolateQuantumEqual() {
//        int phaserThreshold = 5;
//        NuclearService Nuclear = new NuclearService();
//        double Interpolate = Nuclear.interpolateQuantum(40, 2);
//        assertEquals(83.0, Interpolate, 0);
//    }
//
//    @Test
//    public void interpolateQuantumNotEqual() {
//        int phaserThreshold = 5;
//        NuclearService Nuclear = new NuclearService();
//        double Interpolate = Nuclear.interpolateQuantum(38, 2);
//        assertNotEquals(83.0, Interpolate, 0);
//
//    }
//    @Test
//    public void interpolateQuantumEqualTest() {
//        int phaserThreshold = 15;
//        NuclearService Nuclear = new NuclearService();
//        double Interpolate = Nuclear.interpolateQuantum(20, 2);
//        assertEquals(43.0, Interpolate, 0);
//
//    }
//    @Test(expected = IllegalStateException.class)
//    public void interpolateQuantumException() {
//        int phaserThreshold = 5;
//        NuclearService Nuclear = new NuclearService();
//        Nuclear.interpolateQuantum(4, 1);
//	}
//}