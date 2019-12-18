package ro.victor.unittest.mocks.aproapeIntegrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import sun.font.ScriptRun;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringPartialMockuit {
    @Autowired
    private BizLogic logic;
    @MockBean
    AuthService mockService;
    @Test
    public void mtest() {
        when(mockService.useruAreDreptul(anyString())).thenReturn(true);
        assertEquals("UNU", logic.m());
    }
}


@Service
@RequiredArgsConstructor
class BizLogic {
    private final AltService service;
    private final AuthService authService;
    public String m() {
        if (!authService.useruAreDreptul("jdoe")) {
            throw new IllegalArgumentException("NTZ");
        }
        return service.m().toUpperCase();
    }
}

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
class MyApp {

}

@Service
class AuthService {
    public boolean useruAreDreptul(String jdoe) {
        //apel pe bune de WS
        throw new RuntimeException("N-am fir");
    }
}
@Service
class AltService {
    public String m() {
        return "unu";
    }
}

