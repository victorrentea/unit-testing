package ro.victor.unittest.spring.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerMvcPortTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
//                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void testUnsecured() throws Exception {
        mvc.perform(get("/unsecured"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }
    // ref https://www.baeldung.com/spring-security-integration-tests
//    @WithMockUser("security")
    @Test
    public void testSecured() throws Exception {
        mvc.perform(get("/secured"))
                .andExpect(status().isOk())
                .andExpect(content().string("99"));
    }


    // TODO prove @MockBean work - copy the testSearch test here.

}
