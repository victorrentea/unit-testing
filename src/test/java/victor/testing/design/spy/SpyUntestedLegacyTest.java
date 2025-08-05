package victor.testing.design.spy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SpyUntestedLegacyTest {

   @Spy
   private ComplexLegacyClass target;

  @Test
  void returnsEmptyForEmptyData() {
      doReturn(Collections.emptyList()).when(target).privateComplex(13);
      assertThat(target.publicComplex(13)).isEmpty();
   }

  @Test
  void returnsFirstDataElement() {
      doReturn(List.of("one","two")).when(target).privateComplex(13);
      assertThat(target.publicComplex(13)).hasValue("one");
   }
   // TODO + 10 tests for Complex Code #1, #2
}