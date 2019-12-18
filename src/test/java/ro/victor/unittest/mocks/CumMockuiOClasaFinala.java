package ro.victor.unittest.mocks;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ClasaDinFramework.class, LocalDate.class})
public class CumMockuiOClasaFinala {

    @Test
    public void x() {
        PowerMockito.mockStatic(ClasaDinFramework.class);
        PowerMockito.when(ClasaDinFramework.someMethod()).thenReturn(1);
        int actual = new DeTestat().m();
        Assert.assertEquals(2, actual);
    }
    @Test
    public void y() {
        LocalDate timpulFixat = LocalDate.parse("2019-01-01");
        PowerMockito.mockStatic(LocalDate.class);
        PowerMockito.when(LocalDate.now()).thenReturn(timpulFixat);
        //macabru
        LocalDate now = LocalDate.now();
        Assert.assertEquals(timpulFixat, now);
    }
}

class DeTestat {

    public int m() {
        return 2 * ClasaDinFramework.someMethod();
    }
}


final class ClasaDinFramework {
    public static int someMethod() {
        throw new IllegalArgumentException();
    }
}