package victor.testing.tdd;

import java.time.LocalDateTime;

public class CustomerService {
   public Customer createCustomer() {

      Customer customer = new Customer();
      customer.setCreateDate(LocalDateTime.now());
      return customer;
   }
}

class Customer {

   private LocalDateTime createDate;

   public void setCreateDate(LocalDateTime createDate) {
      this.createDate = createDate;
   }

   public LocalDateTime getCreateDate() {
      return createDate;
   }
}