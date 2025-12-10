package victor.testing.spring.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.kafka.test.context.EmbeddedKafka;
import victor.testing.spring.repo.SupplierRepo;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka
class FlywayJavaMigrationTest {

    @Autowired
    SupplierRepo supplierRepo;

    @Test
    void javaMigrationInsertedSupplier() {
        assertThat(supplierRepo.findByCode("JAVAMIG")).isPresent();
    }
}
