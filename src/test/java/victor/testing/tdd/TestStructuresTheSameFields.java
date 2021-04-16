package victor.testing.tdd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStructuresTheSameFields {
   @Data
   @AllArgsConstructor
   class SomeStruct {
      private String s1;
      private String s2;
      private String s3;
   }
   @Test
   public void compareStruct() throws JsonProcessingException {
      SomeStruct struct1 = new SomeStruct("a", "b", "c");
      SomeStruct struct2 = new SomeStruct("a", "b", "c");

      String json1 = new ObjectMapper().writeValueAsString(struct1);
      String json2 = new ObjectMapper().writeValueAsString(struct2);
      assertEquals(json2, json1);
   }
}
