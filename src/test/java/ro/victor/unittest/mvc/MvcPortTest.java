package ro.victor.unittest.mvc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MvcPortTest {

//	@Autowired
//    private WebApplicationContext wac;

	@Autowired
    private MockMvc mockMvc;

//    @Before
//    public void setup() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
//    }
    
    @Test
	@DirtiesContext
    public void peaceTest() throws Exception {
    	mockMvc.perform(get("/peace/{ssn}","abc"))
    		.andExpect(status().isOk())
    		.andExpect(header().string("Head-Shot", "true"))
    		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    		.andExpect(jsonPath("$.you.ssn").value("ABC"));
    }
    @Test
    public void peaceTest2() throws Exception {
    	mockMvc.perform(get("/peace/{ssn}","abc"))
    		.andExpect(status().isOk())
    		.andExpect(header().string("Head-Shot", "true"))
    		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    		.andExpect(jsonPath("$.ssn").value("ABC"));
    }

}


