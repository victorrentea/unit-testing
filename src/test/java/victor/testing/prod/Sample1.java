package victor.testing.prod;

import org.apache.kafka.common.errors.ApiException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class Sample1 {

   // in prod code
   private static final String ENTITY_NOT_EXISTS = "Entity %s does not exist with id = %d";

   // test-constant
   private static final Long ID = 13L;

   @Rule
   public ExpectedException expectedException = ExpectedException.none();
   @Mock
   private ContentRestrictionConfiguration contentRestrictionConfiguration;


   @Test
   public void convertToEntity_shouldThrowApiException_ifCategoryIsNotValid() {
      //Given
//      expectedException.expect(ApiException.class);
//      expectedException.expectMessage("::message::");
      AdBreakPolicyDto adBreakPolicyDto = createAdBreakPolicyDto();

      CategoriesDto mister = createValidCategoriesDto().get(0);
      when(contentRestrictionConfiguration.filterCategory(mister)).thenThrow(new ApiException("::message::"));

      //When
      ApiException e = assertThrows(ApiException.class, () -> convertToEntity(adBreakPolicyDto));
      assertThat(e.getMessage()).isEqualTo("::message::");

      //Then
      verify(contentRestrictionConfiguration, never()).filterParentalRating(any());
   }

   /// codu de prod
   private AdBreakPolicy convertToEntity(AdBreakPolicyDto adBreakPolicyDto) {

      contentRestrictionConfiguration.filterCategory( createValidCategoriesDto().get(0)); //      throw new ApiException("::message::");

      contentRestrictionConfiguration.filterParentalRating(""); // costa 10$  , dureaza 1 s

      return null;
   }

   private List<CategoriesDto> createValidCategoriesDto() {
      return List.of(new CategoriesDto());
   }

   public static AdBreakPolicyDto createAdBreakPolicyDto() {

      return new AdBreakPolicyDto().setName("::policy::");
   }

}


class CodProd {

}