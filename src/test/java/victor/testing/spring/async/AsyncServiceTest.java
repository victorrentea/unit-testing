package victor.testing.spring.async;

import static org.assertj.core.api.Assertions.assertThat;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.SupplierRepo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class AsyncServiceTest extends IntegrationTest {
    @Autowired
    AsyncService asyncService;
    @Autowired
    SupplierRepo supplierRepo;

    @BeforeEach
    final void beforeEach() {
        supplierRepo.deleteAll();
    }

    @Test
    void asyncReturning() throws Exception {
        String supplierNameToCheck = "sname";
        CompletableFuture<Long> completableFuture = asyncService.asyncReturning(supplierNameToCheck);

        long actualSupplierId = completableFuture.get();
        Supplier supplier = supplierRepo.findById(actualSupplierId).get();
        assertThat(supplier.getName()).isEqualTo(supplierNameToCheck);
        // TODO assert the supplier is inserted correctly
    }

    @Test
    void asyncFireAndForget() throws Exception {
        String supplierNameToCheck = "sname";
        asyncService.asyncFireAndForget(supplierNameToCheck);

        Awaitility.await().atMost(1, TimeUnit.MINUTES).until(() -> supplierRepo.findByName(supplierNameToCheck).isPresent());
        // TODO assert the supplier is inserted correctly
        //  Tip: use Awaitility
        //  Challenge: pass a null name
    }

    @Test
    void asyncFireAndForgetNull() throws Exception {
        asyncService.asyncFireAndForget(null);

        TimeUnit.SECONDS.sleep(3);

        assertThat(supplierRepo.findAll()).isEmpty();
    }

    //TODO make a test for unique constraint on name of Supplier

    @Test
    void asyncFireAndForgetSpring() throws Exception {
        asyncService.asyncFireAndForgetSpring("sname");

        // TODO assert the supplier is inserted correctly
        //  Tip: disable the @Async annotation: see AsyncConfig
    }
}
