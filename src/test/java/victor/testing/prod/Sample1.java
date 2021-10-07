//package victor.testing.prod;
//
//import org.apache.kafka.common.errors.ApiException;
//
//public class Sample1 {
//
//
//   // in prod code
//   private static final String ENTITY_NOT_EXISTS = "Entity %s does not exist with id = %d";
//
//   // test-constant
//   private static final Long ID = 13L;
//
//   public void convertToEntity_shouldThrowApiException_ifCategoryIsNotValid() {
//      //Given
//      expectedException.expect(ApiException.class);
//      expectedException.expectMessage(String.format(ENTITY_NOT_EXISTS, "Category", "iabId", ID));
//      AdBreakPolicyDto adBreakPolicyDto = createAdBreakPolicyDto("policy");
//      when(contentRestrictionConfiguration.filterCategory(createValidCategoriesDto().get(0))).thenThrow
//          (new ApiException(String.format(ENTITY_NOT_EXISTS, "Category", "iabId", ID)));
//
//      //When
//      convertToEntity(adBreakPolicyDto);
//
//      //Then
//      verify(contentRestrictionConfiguration, times(0)).filterParentalRating(any());
//   }
//
//}
//
//
//class CodProd {
//
//}