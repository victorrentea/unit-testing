package ro.victor.unittest.spring.repo;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class RepoTestBase {
//   protected User currentUser;

   @Before
   public final /*!*/ void initialize() {
      // persit current user
      // set data in request context
      // insert 2-3 tari
   }
}




