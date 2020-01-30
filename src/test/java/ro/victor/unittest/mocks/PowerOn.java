package ro.victor.unittest.mocks;

import org.junit.Assert;
import org.junit.Test;

public class PowerOn {
    @Test
    public void m1() {
        // vreau sa mockuiesc metoda locaIp
        String actual = new CodDeProd().m();
        Assert.assertEquals("???:8080", actual);
    }
}

class CodDeProd {
    public String m() {
        return SingletonuMeu.getInstance().localIp() + ":8080";
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