package ro.victor.unittest.mocks;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CumMockuiOClasaFinala {
    @InjectMocks
    private DeTestat sut;
    @Mock
    ClasaDinFrameworkWrapper wrapper;

    @Test
    public void x() {
        when(wrapper.someMethod()).thenReturn(1);
        int actual = sut.m();
        Assert.assertEquals(2, actual);
    }
}

class DeTestat {
    private final ClasaDinFrameworkWrapper wrapper;

    DeTestat(ClasaDinFrameworkWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public int m() {
        return 2 * wrapper.someMethod();
    }
}

class ClasaDinFrameworkWrapper {
    public int someMethod() {
        return ClasaDinFramework.someMethod();
    }
}

class ClasaDinFramework {
    public static int someMethod() {
        throw new IllegalArgumentException();
    }
}