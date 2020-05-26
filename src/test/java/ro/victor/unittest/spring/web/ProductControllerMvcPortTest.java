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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerMvcPortTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context) // aici mvc isi ia portul din contextul spring
//                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void unsecured() throws Exception {
        mvc.perform(get("/unsecured"))
                .andExpect(status().isOk())
                .andExpect(header().string("Head-Shot", "true"))
                .andExpect(content().string("1"));
    }


    // TODO prove @MockBean work - copy the testSearch test here.

}
