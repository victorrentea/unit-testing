package victor.testing.spring.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;

@Service
public class SomeService {
   @Autowired
   ProductRepo repo;

//   @Transactional(propagation = Propagation.REQUIRES_NEW)
   @Transactional(propagation = Propagation.NOT_SUPPORTED)
   public void method() {
      // no tx here now.
      repo.save(new Product("SUrprise"));
   }
}
