package victor.testing.spring.repo;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import victor.testing.spring.domain.Supplier;

public class CommonDataExtension implements BeforeEachCallback {
    private final Supplier supplier = new Supplier();
    private String status;

    public Supplier getSupplier() {
        return supplier;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        SupplierRepo repo = SpringExtension.getApplicationContext(context).getBean(SupplierRepo.class);
        System.out.println("******** Insert Common data via entity manager: " + repo);
        repo.save(supplier);
    }

//    @Test
//    void test() {
//        String status = bizMethod();
//
//        Assertions.assertThat(status).isNotEqualTo("Completed");
//
//    }
//    public String bizMethod() {
//        status = "started";
//        if (true) throw new IllegalArgumentException("Intentionat");
//        status = "Completed";
//        return status;
//    }
}
