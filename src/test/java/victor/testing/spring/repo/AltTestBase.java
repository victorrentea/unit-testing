package victor.testing.spring.repo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import victor.testing.spring.domain.Supplier;

@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional
public class AltTestBase {
	@Autowired
	private SupplierRepo supplierRepo;
	protected Supplier supplier1;
	protected Supplier supplier2;

	@Before
	public final void persistRefData() {
		supplier1 = supplierRepo.save(new Supplier());
		supplier2 = supplierRepo.save(new Supplier());
	}

}
