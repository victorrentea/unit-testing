package victor.testing.prod;

import org.apache.kafka.common.errors.ApiException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Mockito.*;

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
      expectedException.expect(ApiException.class);
      expectedException.expectMessage(String.format(ENTITY_NOT_EXISTS, "Category", "iabId", ID));
      AdBreakPolicyDto adBreakPolicyDto = createAdBreakPolicyDto("policy");
      when(contentRestrictionConfiguration.filterCategory(createValidCategoriesDto().get(0))).thenThrow
          (new ApiException(String.format(ENTITY_NOT_EXISTS, "Category", "iabId", ID)));

      //When
      convertToEntity(adBreakPolicyDto);

      //Then
      verify(contentRestrictionConfiguration, times(0)).filterParentalRating(any());
   }

   private AdBreakPolicy convertToEntity(AdBreakPolicyDto adBreakPolicyDto) {
    return null;
   }

   private List<CategoriesDto> createValidCategoriesDto() {
      return null;
   }

   public static AdBreakPolicyDto createAdBreakPolicyDto(String policy) {
      return new AdBreakPolicyDto();
   }

}


class CodProd {

}