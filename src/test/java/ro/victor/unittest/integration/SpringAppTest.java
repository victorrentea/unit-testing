package ro.victor.unittest.integration;

import org.dbunit.Assertion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringApp.class)
public class SpringAppTest {
    @Autowired
    private MyService service;

    @MockBean
    private LdapUserRepo repoMock;

    @Test
    public void stuff() {
        Mockito.when(repoMock.getById(1L)).thenReturn(new User("gigel"));
        assertEquals("gigel", service.logic(1L));

        Mockito.verify(repoMock).getById(anyInt());
    }
}
