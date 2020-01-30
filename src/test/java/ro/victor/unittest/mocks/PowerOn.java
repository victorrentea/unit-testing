package ro.victor.unittest.mocks;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

public class PowerOn {
    @Test
    public void m1() {
        // vreau sa mockuiesc metoda locaIp
        String actual = new CodDeProd(dep).m();
        Assert.assertEquals("???:8080", actual);
    }
}

class CodDeProd {
    private final SingletonuMeu dep;

    CodDeProd(SingletonuMeu dep) {
        this.dep = dep;
    }

    public String m() {
        return SingletonuMeu.getInstance().localIp() + ":8080";
    }
}
@Service
class SingletonuMeu {
//    private static SingletonuMeu INSTANCE;

    @PostConstruct
    private void init() {
        //faci chestii scumpe. esti un scump
    }
//    public static SingletonuMeu getInstance() {
//        if (INSTANCE == null) {
//            INSTANCE = new SingletonuMeu();
//        }
//        return INSTANCE;
//    }
    public String localIp() {
        return "ceva";
    }
}