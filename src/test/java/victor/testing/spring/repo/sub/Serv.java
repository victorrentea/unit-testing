package victor.testing.spring.repo.sub;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Serv {
   @Transactional(propagation = Propagation.REQUIRES_NEW)
   public void method() {
      //insertul de aici se commite indiferent de @Transctia ce vine din teste ce se va rolbackui
   }
}
