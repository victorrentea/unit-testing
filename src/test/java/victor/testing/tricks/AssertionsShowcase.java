package victor.testing.tricks;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;

public class AssertionsShowcase {

   @Test
   public void listSize() {
      List<Integer> actual = asList(1, 2, 3);

      // I expect to only get one element
      Assertions.assertThat(actual).hasSize(1);
   }

   @Test
   public void string() {
      String actual = "abcdef";

      Assertions.assertThat(actual);
   }
   @Test
   public void setContent() {
      Set<Integer> actual = new HashSet<>(asList(100,200,300));

      Assertions.assertThat(actual);
   }

}
