package victor.testing.mocks.layers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

   @Mock
   Adapter adapter;
   @InjectMocks
   UserService userService;

   @Test
   void method() {
      User user = new User("jdoe", "First LAST");
      when(adapter.findUserByUsername("jdoe")).thenReturn(user);

      userService.method();
      ///etc...
   }
}