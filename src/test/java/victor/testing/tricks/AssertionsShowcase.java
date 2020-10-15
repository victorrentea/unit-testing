package victor.testing.tricks;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class AssertionsShowcase {

   @Test
   public void listSize() {
      List<Integer> actual = asList(1, 2, 3);

      // I expect to only get one element
//      assertEquals(actual.toString(), 1, actual.size());
      assertThat(actual).hasSize(1);
//      assertThat(actual).allM
   }

   @Test
   public void string() {
      String actual = "abcdef";

//      Assert.assertTrue(actual.startsWith("bcd"));
      Assertions.assertThat(actual).startsWith("bcd");
   }
   @Test
   public void setContent() {
      List<Integer> actual = asList(200,100,300);

//      Assert.assertEquals(new HashSet<>(asList(100,200,300)), new HashSet<>(actual));
//      Assert.assertEquals(new HashSet<>(), actual);
      Assertions.assertThat(actual).containsExactlyInAnyOrder(100,200,300);
   }

}
