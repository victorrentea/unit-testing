package victor.testing.spring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import victor.testing.spring.domain.Product;

import java.util.List;

public interface ProductRepo extends ProductRepoSearch, JpaRepository<Product, Long> {
   @Query("SELECT p FROM Product p where p.name = ?1")
   Product findByName(String name);

//   @Query(nativeQuery = true, value = "SELECT p2.ID FROM PRODUCT p2\n" +
//                                      "                  inner join SUPPLIER S on S.ID = p2.SUPPLIER_ID\n" +
//                                      "    FETCH FIRST 10 ROWS WITH TIES")
//   List<Long> nativeq();

}
