package victor.testing.design.fixturecreep;


import lombok.RequiredArgsConstructor;

import static victor.testing.design.fixturecreep.FeatureFlags.Feature.PORK_SHAWARMA;

//@Service
// class PaymentService { 667 lines
// class ConsumerService { 1000 lines
// class ConsumerValidationService { or better:
// class ValidateCustomerService {

@RequiredArgsConstructor
public class TzatzikiService {
   private final Dependency dependency;

   public void makeTzatziki() {
      if (!dependency.isCucumberAllowed()) {
         throw new IllegalArgumentException();
      }
      // complex logic: 5 ifs
   }
}
