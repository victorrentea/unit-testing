package victor.testing.mocks;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import victor.testing.builder.Customer;
import victor.testing.builder.CustomerValidator;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith({MockitoExtension.class})
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
      when(incomoda.cevaExtern(6)).thenReturn(2); // inveti ce sa returneze metode
//      BDDMockito.given(incomoda.cevaExtern()).willReturn(2);

      // act / when
      int result = deTestat.deBiz(new Customer(), 5);

      // assert / then
      assertEquals(3, result);
      verify(validator).validate(any()); // validezi ca s-a chemat {de cate ori?}
//      BDDMockito.then(validator).should().validate(any());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testEx() {

//      when(validator.validate(any())).thenThrow(new IllegalArgumentException()); // nu compileaza pt ca functia pe care incerci sa o programezi intoarce void
      doThrow(new IllegalArgumentException()).when(validator).validate(any()); // sintaxa ciudata

      // act / when
      deTestat.deBiz(new Customer(), 5);
   }

   @Test
   public void test2() {
      when(incomoda.cu2ParamUnuDeNestat(eq(1), any())).thenReturn("OK");

      assertEquals("OK", incomoda.cu2ParamUnuDeNestat(1, new Customer()));
      verifyNoMoreInteractions(incomoda);
      verify(incomoda, never()).getString();
   }
}

