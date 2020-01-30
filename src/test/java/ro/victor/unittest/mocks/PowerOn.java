package ro.victor.unittest.mocks;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SingletonuMeu.class})
public class PowerOn {
    @Test
    public void m1() {
        PowerMockito.mockStatic(SingletonuMeu.class);
        SingletonuMeu mock = Mockito.mock(SingletonuMeu.class);
        Mockito.when(SingletonuMeu.getInstance()).thenReturn(mock);
        Mockito.when(mock.localIp()).thenReturn("tataie");
        // vreau sa mockuiesc metoda locaIp
        String actual = new CodDeProd().m();
        Assert.assertEquals("tataie:8080", actual);
    }

    @Test
    public void m2() {
        String s = new CodDeProd().m();
        Assert.assertEquals("ceva:8080", s);
    }
    @Test
    public void controllingTime() {
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.currentTimeMillis()).thenReturn(1580380105956L);
        String s = new CodDeProd().m2();
        Assert.assertEquals("E ora 1580380105956", s);
    }
}

class CodDeProd {
    public String m() {
        SingletonuMeu mock = SingletonuMeu.getInstance();
        return mock.localIp() + ":8080";
    }

    public String m2() {
        return "E ora " + System.currentTimeMillis();
    }
}

// acu singletonul e intr-un jar de la altu
// => hacker solution1: extinzi clasa si folosesti subclasa ta.
// => hacker solution2: decompilezi clasa, o modifici si o pui in acelasi pachet in src-ul tau.
class SingletonuMeu {
    private static SingletonuMeu INSTANCE;

    private SingletonuMeu() {
        //faci chestii scumpe. esti un scump
    }
    public static SingletonuMeu getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SingletonuMeu();
        }
        return INSTANCE;
    }
    public String localIp() {
        return "ceva";
    }
}