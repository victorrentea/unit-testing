package victor.testing.mocks.overspecifying;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class PriceTestBase {
   @Mock
   protected UserRepo userRepo;
   protected User user = new User();

   @BeforeEach
   public final void before() {
      Mockito.when(userRepo.findById(1L)).thenReturn(Optional.of(user));
   }
}

interface UserRepo extends JpaRepository<User, Long> {

}
class User {

}