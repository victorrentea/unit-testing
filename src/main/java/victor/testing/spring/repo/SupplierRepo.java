package victor.testing.spring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import victor.testing.spring.domain.Supplier;

public interface SupplierRepo extends JpaRepository<Supplier, Long> {
   Supplier findByName(String supplierName);
//   @Query(value = "SELECT /*+amintiri*/ chior FROM (SELECT )", nativeQuery = true)
//   int vreoMetoDeRepoNative();

   @Query("SELECT count(p) FROM Product p") // JPQL
   int oMetoda();
}
