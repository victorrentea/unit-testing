package victor.testing.mocks;


import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class MockStaticTest {
   @Test
   public void mockUtilMethod() {
      assertThat(SomeUtil.staticMethod(3)).isEqualTo(-1);
      try (MockedStatic<SomeUtil> b = Mockito.mockStatic(SomeUtil.class)) {
         b.when(() -> SomeUtil.staticMethod(3)).thenReturn(1);
         assertThat(new LegacyProdCode().prod()).isEqualTo(2);
      }
   }

   @Test
   public void mockTime() {
      LocalDateTime fixed = LocalDateTime.parse("2021-09-29T23:07:01");
      try (MockedStatic<LocalDateTime> b = Mockito.mockStatic(LocalDateTime.class)) {
         b.when(LocalDateTime::now).thenReturn(fixed);

         System.out.println(LocalDateTime.now());
      }
   }

}

