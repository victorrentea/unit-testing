package victor.testing.mocks;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MockStaticTest {
   @Test
   public void mockLibraryMethod() {
      // prove the original behavior
      assertThat(SomeLibrary.staticMethod(3)).isEqualTo(-1);

      //hack time
      try (MockedStatic<SomeLibrary> mock = mockStatic(SomeLibrary.class)) {
         mock.when(() -> SomeLibrary.staticMethod(3)).thenReturn(1);

         // call tested code
         int actual = new ProdCode().prod(3);

         assertThat(actual).isEqualTo(2);
      }
   }


   @Test
   public void mockTime() {
      LocalDateTime fixed = LocalDateTime.parse("2021-09-29T23:07:01");
      try (MockedStatic<LocalDateTime> mock = mockStatic(LocalDateTime.class)) {
         mock.when(LocalDateTime::now).thenReturn(fixed);
         System.out.println(LocalDateTime.now());
      }
   }
   @Test
   @Disabled("doesn't work!")
   public void mock_java_util_Date() throws ParseException {
      long millis = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
          .parse("2021-09-29 23:07:01").getTime();

      try (MockedStatic<System> mock = mockStatic(System.class)) {
         mock.when(System::currentTimeMillis).thenReturn(millis);
         System.out.println(new Date());
      }
   }
}

