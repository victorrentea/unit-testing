package victor.testing.spring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.testing.spring.entity.Supplier;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface SupplierRepo extends JpaRepository<Supplier, Long> {
   Optional<Supplier> findByName(String name);

   Optional<Supplier> findByCode(String code);
}

//class SupplierRepoFakeDoarPtTeste implements SupplierRepo {
//   private Map<Long, Supplier> suppliers = new HashMap<>();
//
//   public void addSupplier(Supplier supplier) {
//      suppliers.put(supplier.getId(), supplier);
//   }
//
//   public Optional<Supplier> findByName(String name) {
//      return suppliers.values().stream().filter(s -> s.getName().equals(name)).findFirst();
//   }
//   public Optional<Supplier> findByCode(String code) {
//      return suppliers.values().stream().filter(s -> s.getCode().equals(code)).findFirst();
//   }
//}