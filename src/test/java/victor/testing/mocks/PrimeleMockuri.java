package victor.testing.mocks;

import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import victor.testing.builder.Customer;
import victor.testing.builder.CustomerValidator;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PrimeleMockuri {
   @Mock
   private Incomoda incomoda;// = Mockito.mock(Incomoda.class);
   @Mock
   private CustomerValidator validator;// = Mockito.mock(CustomerValidator.class);
   @InjectMocks
   private UserService deTestat;// = new UserService(incomoda);
   @Test
   public void test() {
      // arrange / given
      when(incomoda.cevaExtern()).thenReturn(2); // inveti ce sa returneze metode

      // act / when
      int result = deTestat.deBiz(new Customer());

      // assert / then
      assertEquals(3, result);
      verify(validator).validate(any()); // validezi ca s-a chemat {de cate ori?}
   }

   @Test(expected = IllegalArgumentException.class)
   public void testEx() {
//      when(validator.validate(any())).thenThrow(new IllegalArgumentException()); // nu compileaza pt ca functia pe care incerci sa o programezi intoarce void
      doThrow(new IllegalArgumentException()).when(validator).validate(any()); // sintaxa ciudata

      // act / when
      deTestat.deBiz(new Customer());
   }
}

