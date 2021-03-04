package victor.testing.mocks;

import lombok.RequiredArgsConstructor;
import victor.testing.builder.Customer;
import victor.testing.builder.CustomerValidator;

import java.util.List;

@RequiredArgsConstructor
public class UserService {
   private final Incomoda incomoda;
   private final CustomerValidator validator;

   public int deBiz(Customer customer, int i) {
      validator.validate(customer);
      System.out.println(incomoda.cevaExtern(-1));
      System.out.println(incomoda.getList());
      System.out.println(incomoda.getString());
      return incomoda.cevaExtern(i + 1) + 1;
   }
}
class Incomoda {
   public int cevaExtern(int i) { // daca nu faci when ..then pe asta da 0
      throw new IllegalArgumentException(" nu pot fi chemat din teste");
   }

   public List<String> getList() {  // daca nu faci when ..then pe asta da emptyList()
      throw new IllegalArgumentException();
   }

   public String getString() { // daca nu faci when ..then pe asta da null
      return "aaaaa";
   }

   public String cu2ParamUnuDeNestat(int i, Customer c) {
      return " nu conteaza ca o mockuiesc";
   }
}