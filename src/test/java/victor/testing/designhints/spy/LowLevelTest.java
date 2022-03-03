package victor.testing.designhints.spy;

import org.junit.jupiter.api.Test;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class LowLevelTest {

   @Test
   void low() { // + 5 more tests like this
      Order oldOrder = new Order().setCreationDate(now().minusMonths(2));
      assertThatThrownBy(() -> new LowLevel().low(oldOrder))
          .hasMessageContaining("old");
   }
}