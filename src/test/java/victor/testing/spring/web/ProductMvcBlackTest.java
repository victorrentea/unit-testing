package victor.testing.spring.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.SupplierRepo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductMvcBlackTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SafetyClient safetyClient;
    @Autowired
    private SupplierRepo supplierRepo;

    @Test
    public void testSearch() throws Exception {
        // given
        Long supplierId = supplierRepo.save(new Supplier().setActive(true)).getId();
        when(safetyClient.isSafe("UPC")).thenReturn(true);

        // language=json
        String createJson = String.format("{\"name\": \"Tree\", \"supplierId\": \"%d\", \"upc\": \"UPC\"}", supplierId);

        // TODO create via REST CALL

        // TODO search via REST CALL
    }


}
